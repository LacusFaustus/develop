package ru.yandex.javacourse.http.handler;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements com.sun.net.httpserver.HttpHandler {
    protected void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    protected void sendSuccess(HttpExchange exchange, String response) throws IOException {
        sendResponse(exchange, response, 200);
    }

    protected void sendCreated(HttpExchange exchange, String response) throws IOException {
        sendResponse(exchange, response, 201);
    }

    protected void sendNotFound(HttpExchange exchange, String response) throws IOException {
        sendResponse(exchange, response, 404);
    }

    protected void sendNotAcceptable(HttpExchange exchange, String response) throws IOException {
        sendResponse(exchange, response, 406);
    }

    protected void sendInternalError(HttpExchange exchange, String response) throws IOException {
        sendResponse(exchange, response, 500);
    }

    protected void sendBadRequest(HttpExchange exchange, String response) throws IOException {
        sendResponse(exchange, response, 400);
    }

    protected String readRequestBody(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }
}