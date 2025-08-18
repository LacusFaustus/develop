package ru.yandex.javacourse.http.handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacourse.exception.ManagerSaveException;
import ru.yandex.javacourse.model.Epic;
import ru.yandex.javacourse.service.TaskManager;

import java.io.IOException;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public EpicsHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");

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

    private void handleGet(HttpExchange exchange, String[] pathParts) throws IOException {
        if (pathParts.length == 2) {
            List<Epic> epics = taskManager.getAllEpics();
            sendSuccess(exchange, gson.toJson(epics));
        } else if (pathParts.length == 3) {
            int id = Integer.parseInt(pathParts[2]);
            Epic epic = taskManager.getEpicById(id);
            if (epic != null) {
                sendSuccess(exchange, gson.toJson(epic));
            } else {
                sendNotFound(exchange, "{\"message\":\"Эпик с id=" + id + " не найден\"}");
            }
        } else {
            sendNotFound(exchange, "{\"message\":\"Неверный URL\"}");
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        try {
            Epic epic = gson.fromJson(requestBody, Epic.class);
            if (epic == null) {
                sendBadRequest(exchange, "{\"message\":\"Некорректный формат эпика\"}");
                return;
            }

            if (epic.getId() == 0) {
                int id = taskManager.createEpic(epic);
                sendCreated(exchange, gson.toJson(taskManager.getEpicById(id)));
            } else {
                taskManager.updateEpic(epic);
                sendSuccess(exchange, gson.toJson(epic));
            }
        } catch (JsonSyntaxException e) {
            sendBadRequest(exchange, "{\"message\":\"Некорректный JSON\"}");
        }
    }

    private void handleDelete(HttpExchange exchange, String[] pathParts) throws IOException {
        if (pathParts.length == 3) {
            int id = Integer.parseInt(pathParts[2]);
            taskManager.deleteEpicById(id);
            sendSuccess(exchange, "{\"message\":\"Эпик с id=" + id + " удален\"}");
        } else {
            sendNotFound(exchange, "{\"message\":\"Неверный URL\"}");
        }
    }
}