package ru.yandex.javacourse.http;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.model.Task;
import ru.yandex.javacourse.model.Status;
import ru.yandex.javacourse.service.InMemoryTaskManager;
import ru.yandex.javacourse.service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    private HttpTaskServer server;
    private TaskManager manager;
    private HttpClient client;
    private Gson gson;
    private LocalDateTime testTime;

    @BeforeEach
    void setUp() throws IOException {
        manager = new InMemoryTaskManager();
        server = new HttpTaskServer(manager);
        server.start();
        client = HttpClient.newHttpClient();
        gson = server.getGson();
        testTime = LocalDateTime.now();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void testCreateTask() throws IOException, InterruptedException {
        // Подготовка JSON для создания задачи
        String taskJson = "{"
                + "\"name\":\"Test Task\","
                + "\"description\":\"Test Description\","
                + "\"status\":\"NEW\","
                + "\"startTime\":\"" + testTime + "\","
                + "\"duration\":30"
                + "}";

        // Отправка POST-запроса
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void testGetPrioritizedTasks() throws IOException, InterruptedException {
        // Создаем две задачи с полным набором параметров
        Task task1 = new Task("Task 1", "Description 1", Status.NEW,
                testTime.plusHours(2), Duration.ofMinutes(30));
        manager.createTask(task1);

        Task task2 = new Task("Task 2", "Description 2", Status.NEW,
                testTime.plusHours(1), Duration.ofMinutes(30));
        manager.createTask(task2);

        // Получаем приоритизированный список
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void testInvalidTaskCreation() throws IOException, InterruptedException {
        // Неправильный JSON (отсутствует обязательное поле name)
        String invalidJson = "{\"description\":\"Only description\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(invalidJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Добавим вывод ответа для отладки
        System.out.println("Response status: " + response.statusCode());
        System.out.println("Response body: " + response.body());

        assertEquals(400, response.statusCode(),
                "Сервер должен возвращать 400 при невалидных данных");
    }
}