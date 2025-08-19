package ru.yandex.javacourse.http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacourse.model.Task;
import ru.yandex.javacourse.service.TaskManager;

import java.io.IOException;
import java.util.List;

/**
 * Обработчик HTTP-запросов для получения приоритизированного списка задач.
 */
public final class PrioritizedHandler extends BaseHttpHandler {
    /** Менеджер задач. */
    private final TaskManager taskManager;
    /** Объект для JSON-сериализации. */
    private final Gson gson;

    /**
     * Конструктор обработчика приоритизированных задач.
     * @param taskManager менеджер задач
     * @param gson объект для JSON-сериализации
     */
    public PrioritizedHandler(final TaskManager taskManager, final Gson gson) {
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
            if ("GET".equals(exchange.getRequestMethod())) {
                final List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
                sendSuccess(exchange, gson.toJson(prioritizedTasks));
            } else {
                sendNotFound(exchange, "{\"message\":\"Метод не поддерживается\"}");
            }
        } catch (Exception e) {
            sendInternalError(exchange,
                    "{\"message\":\"Внутренняя ошибка сервера\"}");
        }
    }
}