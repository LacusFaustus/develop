package ru.yandex.javacourse.http.handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacourse.exception.ManagerSaveException;
import ru.yandex.javacourse.model.Subtask;
import ru.yandex.javacourse.service.TaskManager;

import java.io.IOException;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public SubtasksHandler(TaskManager taskManager, Gson gson) {
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
            List<Subtask> subtasks = taskManager.getAllSubtasks();
            sendSuccess(exchange, gson.toJson(subtasks));
        } else if (pathParts.length == 3) {
            int id = Integer.parseInt(pathParts[2]);
            Subtask subtask = taskManager.getSubtaskById(id);
            if (subtask != null) {
                sendSuccess(exchange, gson.toJson(subtask));
            } else {
                sendNotFound(exchange, "{\"message\":\"Подзадача с id=" + id + " не найдена\"}");
            }
        } else {
            sendNotFound(exchange, "{\"message\":\"Неверный URL\"}");
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        try {
            Subtask subtask = gson.fromJson(requestBody, Subtask.class);
            if (subtask == null) {
                sendBadRequest(exchange, "{\"message\":\"Некорректный формат подзадачи\"}");
                return;
            }

            if (subtask.getId() == 0) {
                int id = taskManager.createSubtask(subtask);
                sendCreated(exchange, gson.toJson(taskManager.getSubtaskById(id)));
            } else {
                taskManager.updateSubtask(subtask);
                sendSuccess(exchange, gson.toJson(subtask));
            }
        } catch (JsonSyntaxException e) {
            sendBadRequest(exchange, "{\"message\":\"Некорректный JSON\"}");
        }
    }

    private void handleDelete(HttpExchange exchange, String[] pathParts) throws IOException {
        if (pathParts.length == 3) {
            int id = Integer.parseInt(pathParts[2]);
            taskManager.deleteSubtaskById(id);
            sendSuccess(exchange, "{\"message\":\"Подзадача с id=" + id + " удалена\"}");
        } else {
            sendNotFound(exchange, "{\"message\":\"Неверный URL\"}");
        }
    }
}