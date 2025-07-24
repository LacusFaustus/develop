package ru.yandex.javacourse.service;

import org.junit.jupiter.api.*;
import ru.yandex.javacourse.model.*;
import java.util.List; // Добавляем импорт
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager manager;
    private Task task;
    private Epic epic;
    private Subtask subtask; // Добавляем объявление переменной

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
        task = new Task("Test task", "Test description");
        epic = new Epic("Test epic", "Test description");
        manager.createEpic(epic);
        subtask = new Subtask("Test subtask", "Test description", epic.getId()); // Инициализируем здесь
    }

    @Test
    @DisplayName("Должен создавать и возвращать задачу с тем же ID")
    void shouldCreateAndRetrieveTaskWithSameId() {
        int taskId = manager.createTask(task);
        Task retrieved = manager.getTaskById(taskId);
        assertEquals(taskId, retrieved.getId());
    }

    // Тест с использованием subtask
    @Test
    @DisplayName("Должен создавать и возвращать подзадачу")
    void shouldCreateAndRetrieveSubtask() {
        int subtaskId = manager.createSubtask(subtask);
        Subtask retrieved = manager.getSubtaskById(subtaskId);
        assertEquals(subtaskId, retrieved.getId());
    }
}