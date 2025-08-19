package ru.yandex.javacourse.http.handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacourse.exception.ManagerSaveException;
import ru.yandex.javacourse.model.Epic;
import ru.yandex.javacourse.service.TaskManager;

import java.io.IOException;
import java.util.List;

/**
 * Обработчик HTTP-запросов для работы с эпиками.
 */
public class EpicsHandler extends BaseHttpHandler {
    /** Менеджер задач. */
    private final TaskManager taskManager;
    /** Объект для JSON-сериализации. */
    private final Gson gson;

    /**
     * Конструктор обработчика эпиков.
     * @param taskManager менеджер задач
     * @param gson объект для JSON-сериализации
     */
    public EpicsHandler(final TaskManager taskManager, final Gson gson) {
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
            final List<Epic> epics = taskManager.getAllEpics();
            sendSuccess(exchange, gson.toJson(epics));
        } else if (pathParts.length == expectedPathPartsLength) {
            final int id = Integer.parseInt(pathParts[2]);
            final Epic epic = taskManager.getEpicById(id);
            if (epic != null) {
                sendSuccess(exchange, gson.toJson(epic));
            } else {
                sendNotFound(exchange, "{\"message\":\"Эпик с id=" + id + " не найден\"}");
            }
        } else {
            sendNotFound(exchange, "{\"message\":\"Неверный URL\"}");
        }
    }

    private void handlePost(final HttpExchange exchange) throws IOException {
        final String requestBody = readRequestBody(exchange);
        try {
            final Epic epic = gson.fromJson(requestBody, Epic.class);
            if (epic == null) {
                sendBadRequest(exchange, "{\"message\":\"Некорректный формат эпика\"}");
                return;
            }

            if (epic.getId() == 0) {
                final int id = taskManager.createEpic(epic);
                sendCreated(exchange, gson.toJson(taskManager.getEpicById(id)));
            } else {
                taskManager.updateEpic(epic);
                sendSuccess(exchange, gson.toJson(epic));
            }
        } catch (JsonSyntaxException e) {
            sendBadRequest(exchange, "{\"message\":\"Некорректный JSON\"}");
        }
    }

    private void handleDelete(final HttpExchange exchange, final String[] pathParts)
            throws IOException {
        final int expectedPathPartsLength = 3;
        if (pathParts.length == expectedPathPartsLength) {
            final int id = Integer.parseInt(pathParts[2]);
            taskManager.deleteEpicById(id);
            sendSuccess(exchange, "{\"message\":\"Эпик с id=" + id + " удален\"}");
        } else {
            sendNotFound(exchange, "{\"message\":\"Неверный URL\"}");
        }
    }
}