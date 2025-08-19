package ru.yandex.javacourse.http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacourse.model.Task;
import ru.yandex.javacourse.service.TaskManager;

import java.io.IOException;
import java.util.List;

/**
 * Обработчик HTTP-запросов для получения истории просмотров задач.
 */
public final class HistoryHandler extends BaseHttpHandler {
    /** Менеджер задач для получения истории. */
    private final TaskManager taskManager;

    /** Объект для сериализации/десериализации в JSON. */
    private final Gson gson;

    /**
     * Создает новый обработчик истории.
     *
     * @param taskManager менеджер задач для работы с историей
     * @param gson объект для JSON-сериализации
     */
    public HistoryHandler(final TaskManager taskManager, final Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    /**
     * Обрабатывает HTTP-запрос.
     *
     * @param exchange HTTP-обмен для обработки запроса и формирования ответа
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    public void handle(final HttpExchange exchange) throws IOException {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                List<Task> history = taskManager.getHistory();
                sendSuccess(exchange, gson.toJson(history));
            } else {
                sendNotFound(exchange,
                        "{\"message\":\"Метод не поддерживается\"}");
            }
        } catch (Exception e) {
            sendInternalError(exchange,
                    "{\"message\":\"Внутренняя ошибка сервера\"}");
        }
    }
}