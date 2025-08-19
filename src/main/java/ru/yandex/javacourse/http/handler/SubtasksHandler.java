package ru.yandex.javacourse.http.handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacourse.exception.ManagerSaveException;
import ru.yandex.javacourse.model.Subtask;
import ru.yandex.javacourse.service.TaskManager;

import java.io.IOException;
import java.util.List;

/**
 * Обработчик HTTP-запросов для работы с подзадачами.
 */
public final class SubtasksHandler extends BaseHttpHandler {
    /** Менеджер задач. */
    private final TaskManager taskManager;
    /** Объект для JSON-сериализации. */
    private final Gson gson;

    /**
     * Конструктор обработчика подзадач.
     * @param taskManager менеджер задач
     * @param gson объект для JSON-сериализации
     */
    public SubtasksHandler(final TaskManager taskManager, final Gson gson) {
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
            final List<Subtask> subtasks = taskManager.getAllSubtasks();
            sendSuccess(exchange, gson.toJson(subtasks));
        } else if (pathParts.length == expectedPathPartsLength) {
            final int id = Integer.parseInt(pathParts[2]);
            final Subtask subtask = taskManager.getSubtaskById(id);
            if (subtask != null) {
                sendSuccess(exchange, gson.toJson(subtask));
            } else {
                sendNotFound(exchange,
                        "{\"message\":\"Подзадача с id=" + id + " не найдена\"}");
            }
        } else {
            sendNotFound(exchange, "{\"message\":\"Неверный URL\"}");
        }
    }

    private void handlePost(final HttpExchange exchange) throws IOException {
        final String requestBody = readRequestBody(exchange);
        try {
            final Subtask subtask = gson.fromJson(requestBody, Subtask.class);
            if (subtask == null) {
                sendBadRequest(exchange, "{\"message\":\"Некорректный формат подзадачи\"}");
                return;
            }

            if (subtask.getId() == 0) {
                final int id = taskManager.createSubtask(subtask);
                sendCreated(exchange, gson.toJson(taskManager.getSubtaskById(id)));
            } else {
                taskManager.updateSubtask(subtask);
                sendSuccess(exchange, gson.toJson(subtask));
            }
        } catch (JsonSyntaxException e) {
            sendBadRequest(exchange, "{\"message\":\"Некорректный JSON\"}");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleDelete(final HttpExchange exchange, final String[] pathParts)
            throws IOException {
        final int expectedPathPartsLength = 3;
        if (pathParts.length == expectedPathPartsLength) {
            final int id = Integer.parseInt(pathParts[2]);
            taskManager.deleteSubtaskById(id);
            sendSuccess(exchange, "{\"message\":\"Подзадача с id=" + id + " удалена\"}");
        } else {
            sendNotFound(exchange, "{\"message\":\"Неверный URL\"}");
        }
    }
}