package ru.yandex.javacourse.http.handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacourse.exception.ManagerSaveException;
import ru.yandex.javacourse.model.Task;
import ru.yandex.javacourse.service.TaskManager;

import java.io.IOException;
import java.util.List;

/**
 * Обработчик HTTP-запросов для работы с задачами.
 */
public final class TasksHandler extends BaseHttpHandler {
    /** Менеджер задач. */
    private final TaskManager taskManager;
    /** Объект для JSON-сериализации. */
    private final Gson gson;

    /**
     * Конструктор обработчика задач.
     * @param taskManager менеджер задач
     * @param gson объект для JSON-сериализации
     */
    public TasksHandler(final TaskManager taskManager, final Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    /**
     * Обрабатывает HTTP-запрос.
     * @param exchange HTTP-обмен
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    public void handle(final HttpExchange exchange) throws IOException {
        try {
            final String method = exchange.getRequestMethod();
            final String path = exchange.getRequestURI().getPath();
            final String[] pathParts = path.split("/");

            switch (method) {
                case "GET":
                    handleGet(exchange, pathParts);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange, pathParts);
                    break;
                default:
                    sendNotFound(exchange, "{\"message\":\"Метод не поддерживается\"}");
            }
        } catch (NumberFormatException e) {
            sendBadRequest(exchange, "{\"message\":\"Некорректный формат ID\"}");
        } catch (ManagerSaveException e) {
            sendNotAcceptable(exchange, "{\"message\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            sendInternalError(exchange, "{\"message\":\"Внутренняя ошибка сервера\"}");
        }
    }

    private void handleGet(final HttpExchange exchange, final String[] pathParts)
            throws IOException {
        final int expectedPathPartsLength = 3;
        if (pathParts.length == 2) {
            final List<Task> tasks = taskManager.getAllTasks();
            sendSuccess(exchange, gson.toJson(tasks));
        } else if (pathParts.length == expectedPathPartsLength) {
            final int id = Integer.parseInt(pathParts[2]);
            final Task task = taskManager.getTaskById(id);
            if (task != null) {
                sendSuccess(exchange, gson.toJson(task));
            } else {
                sendNotFound(exchange,
                        "{\"message\":\"Задача с id=" + id + " не найдена\"}");
            }
        } else {
            sendNotFound(exchange, "{\"message\":\"Неверный URL\"}");
        }
    }

    private void handlePost(final HttpExchange exchange) throws IOException {
        final String requestBody = readRequestBody(exchange);
        try {
            final Task task = gson.fromJson(requestBody, Task.class);

            if (task == null || task.getName() == null || task.getName().isEmpty()) {
                sendBadRequest(exchange, "{\"message\":\"Не указано название задачи\"}");
                return;
            }

            if (task.getId() == 0) {
                final int id = taskManager.createTask(task);
                sendCreated(exchange, gson.toJson(taskManager.getTaskById(id)));
            } else {
                taskManager.updateTask(task);
                sendSuccess(exchange, gson.toJson(task));
            }
        } catch (JsonSyntaxException e) {
            sendBadRequest(exchange, "{\"message\":\"Некорректный формат JSON\"}");
        } catch (Exception e) {
            sendInternalError(exchange, "{\"message\":\"Внутренняя ошибка сервера\"}");
        }
    }

    private void handleDelete(final HttpExchange exchange, final String[] pathParts)
            throws IOException {
        final int expectedPathPartsLength = 3;
        if (pathParts.length == expectedPathPartsLength) {
            final int id = Integer.parseInt(pathParts[2]);
            taskManager.deleteTaskById(id);
            sendSuccess(exchange, "{\"message\":\"Задача с id=" + id + " удалена\"}");
        } else {
            sendNotFound(exchange, "{\"message\":\"Неверный URL\"}");
        }
    }
}