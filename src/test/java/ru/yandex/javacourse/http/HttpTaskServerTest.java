package ru.yandex.javacourse.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.javacourse.http.handler.HistoryHandler;
import ru.yandex.javacourse.http.handler.PrioritizedHandler;
import ru.yandex.javacourse.service.InMemoryTaskManager;
import ru.yandex.javacourse.service.TaskManager;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;

/**
 * HTTP-сервер для работы с менеджером задач.
 */
final class HttpTaskServer {
    /** Порт по умолчанию для сервера. */
    private static final int DEFAULT_PORT = 8080;
    /** HTTP-сервер. */
    private HttpServer server;
    /** Менеджер задач. */
    private final TaskManager taskManager;
    /** Объект для работы с JSON. */
    private final Gson gson;
    /** Порт сервера. */
    private final int port;

    /**
     * Создает сервер с портом по умолчанию.
     *
     * @param taskManager менеджер задач
     * @throws IOException если не удалось создать сервер
     */
    public HttpTaskServer(final TaskManager taskManager) throws IOException {
        this(taskManager, DEFAULT_PORT);
    }

    /**
     * Создает сервер с указанным портом.
     *
     * @param taskManager менеджер задач
     * @param port порт для сервера
     * @throws IOException если не удалось создать сервер
     */
    public HttpTaskServer(final TaskManager taskManager, final int port)
            throws IOException {
        this.taskManager = taskManager;
        this.port = port;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    /**
     * Запускает сервер.
     */
    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/tasks", new ru.yandex.javacourse.http.TaskHandler(taskManager, gson) {
                /**
                 * Handle the given request and generate an appropriate response.
                 * See {@link HttpExchange} for a description of the steps
                 * involved in handling an exchange.
                 *
                 * @param exchange the exchange containing the request from the
                 *                 client and used to send the response
                 * @throws NullPointerException if exchange is {@code null}
                 * @throws IOException          if an I/O error occurs
                 */
                @Override
                public void handle(HttpExchange exchange) throws IOException {

                }
            });
            server.createContext("/subtasks", new SubtaskHandler(taskManager, gson) {
                /**
                 * Handle the given request and generate an appropriate response.
                 * See {@link HttpExchange} for a description of the steps
                 * involved in handling an exchange.
                 *
                 * @param exchange the exchange containing the request from the
                 *                 client and used to send the response
                 * @throws NullPointerException if exchange is {@code null}
                 * @throws IOException          if an I/O error occurs
                 */
                @Override
                public void handle(HttpExchange exchange) throws IOException {

                }
            });
            server.createContext("/epics", new EpicHandler(taskManager, gson) {
                /**
                 * Handle the given request and generate an appropriate response.
                 * See {@link HttpExchange} for a description of the steps
                 * involved in handling an exchange.
                 *
                 * @param exchange the exchange containing the request from the
                 *                 client and used to send the response
                 * @throws NullPointerException if exchange is {@code null}
                 * @throws IOException          if an I/O error occurs
                 */
                @Override
                public void handle(HttpExchange exchange) throws IOException {

                }
            });
            server.createContext("/history", new HistoryHandler(taskManager, gson));
            server.createContext("/prioritized",
                    new PrioritizedHandler(taskManager, gson));
            server.start();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось запустить сервер", e);
        }
    }

    /**
     * Останавливает сервер.
     */
    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    /**
     * Возвращает менеджер задач.
     *
     * @return менеджер задач
     */
    public TaskManager getTaskManager() {
        return taskManager;
    }

    /**
     * Возвращает объект для работы с JSON.
     *
     * @return объект Gson
     */
    public Gson getGson() {
        return gson;
    }

    /**
     * Точка входа для запуска сервера.
     *
     * @param args аргументы командной строки (не используются)
     * @throws IOException если не удалось создать сервер
     */
    public static void main(final String[] args) throws IOException {
        TaskManager manager;
        manager = new InMemoryTaskManager();
        HttpTaskServer server = new HttpTaskServer(manager);
        server.start();
        System.out.println("Сервер запущен на порту " + DEFAULT_PORT);
    }
}