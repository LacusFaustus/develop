package ru.yandex.javacourse.http.handler;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Базовый класс для обработчиков HTTP-запросов.
 * Предоставляет общие методы для отправки ответов.
 */
public abstract class BaseHttpHandler implements com.sun.net.httpserver.HttpHandler {
    /** HTTP статус код: Успешный запрос. */
    private static final int HTTP_OK = 200;
    /** HTTP статус код: Ресурс создан. */
    private static final int HTTP_CREATED = 201;
    /** HTTP статус код: Не найдено. */
    private static final int HTTP_NOT_FOUND = 404;
    /** HTTP статус код: Неприемлемо. */
    private static final int HTTP_NOT_ACCEPTABLE = 406;
    /** HTTP статус код: Внутренняя ошибка сервера. */
    private static final int HTTP_INTERNAL_ERROR = 500;
    /** HTTP статус код: Неверный запрос. */
    private static final int HTTP_BAD_REQUEST = 400;

    /**
     * Отправляет HTTP-ответ с указанным статусом.
     * @param exchange объект HTTP-обмена
     * @param response тело ответа
     * @param statusCode HTTP статус код
     * @throws IOException если произошла ошибка ввода-вывода
     */
    protected final void sendResponse(final HttpExchange exchange,
                                      final String response,
                                      final int statusCode) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        final byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    /**
     * Отправляет успешный HTTP-ответ (200 OK).
     * @param exchange объект HTTP-обмена
     * @param response тело ответа
     * @throws IOException если произошла ошибка ввода-вывода
     */
    protected final void sendSuccess(final HttpExchange exchange,
                                     final String response) throws IOException {
        sendResponse(exchange, response, HTTP_OK);
    }

    /**
     * Отправляет HTTP-ответ о создании ресурса (201 Created).
     * @param exchange объект HTTP-обмена
     * @param response тело ответа
     * @throws IOException если произошла ошибка ввода-вывода
     */
    protected final void sendCreated(final HttpExchange exchange,
                                     final String response) throws IOException {
        sendResponse(exchange, response, HTTP_CREATED);
    }

    /**
     * Отправляет HTTP-ответ о ненайденном ресурсе (404 Not Found).
     * @param exchange объект HTTP-обмена
     * @param response тело ответа
     * @throws IOException если произошла ошибка ввода-вывода
     */
    protected final void sendNotFound(final HttpExchange exchange,
                                      final String response) throws IOException {
        sendResponse(exchange, response, HTTP_NOT_FOUND);
    }

    /**
     * Отправляет HTTP-ответ о неприемлемом запросе (406 Not Acceptable).
     * @param exchange объект HTTP-обмена
     * @param response тело ответа
     * @throws IOException если произошла ошибка ввода-вывода
     */
    protected final void sendNotAcceptable(final HttpExchange exchange,
                                           final String response) throws IOException {
        sendResponse(exchange, response, HTTP_NOT_ACCEPTABLE);
    }

    /**
     * Отправляет HTTP-ответ о внутренней ошибке сервера (500 Internal Error).
     * @param exchange объект HTTP-обмена
     * @param response тело ответа
     * @throws IOException если произошла ошибка ввода-вывода
     */
    protected final void sendInternalError(final HttpExchange exchange,
                                           final String response) throws IOException {
        sendResponse(exchange, response, HTTP_INTERNAL_ERROR);
    }

    /**
     * Отправляет HTTP-ответ о неверном запросе (400 Bad Request).
     * @param exchange объект HTTP-обмена
     * @param response тело ответа
     * @throws IOException если произошла ошибка ввода-вывода
     */
    protected final void sendBadRequest(final HttpExchange exchange,
                                        final String response) throws IOException {
        sendResponse(exchange, response, HTTP_BAD_REQUEST);
    }

    /**
     * Читает тело HTTP-запроса.
     * @param exchange объект HTTP-обмена
     * @return содержимое тела запроса
     * @throws IOException если произошла ошибка ввода-вывода
     */
    protected final String readRequestBody(final HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }
}