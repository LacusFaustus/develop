package ru.yandex.javacourse.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacourse.service.TaskManager;

public abstract class SubtaskHandler implements HttpHandler {
    public SubtaskHandler(TaskManager taskManager, Gson gson) {
    }
}
