package ru.yandex.javacourse.service;

import org.junit.jupiter.api.*;
import ru.yandex.javacourse.model.*;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager manager;
    private Task task;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
        task = new Task("Test task", "Test description");
        epic = new Epic("Test epic", "Test description");
        manager.createEpic(epic);
        subtask = new Subtask("Test subtask", "Test description", epic.getId());
    }

    @Test
    @DisplayName("Should create and return task with same ID")
    void shouldCreateAndRetrieveTaskWithSameId() {
        int taskId = manager.createTask(task);
        Task retrieved = manager.getTaskById(taskId);
        assertEquals(taskId, retrieved.getId(), "ID созданной задачи должно совпадать с полученным ID");
    }

    @Test
    @DisplayName("Should create and return subtask")
    void shouldCreateAndRetrieveSubtask() {
        // Создаем эпик
        Epic epic = new Epic("Test epic", "Test description");
        int epicId = manager.createEpic(epic);

        // Создаем подзадачу
        Subtask subtask = new Subtask("Test subtask", "Test description", epicId);
        int subtaskId = manager.createSubtask(subtask);

        // Получаем подзадачу
        Subtask retrieved = manager.getSubtaskById(subtaskId);

        // Проверяем
        assertNotNull(retrieved, "Подзадача не должна быть null");
        assertEquals(subtaskId, retrieved.getId(), "ID подзадачи должно совпадать");
        assertEquals(epicId, retrieved.getEpicId(), "Epic ID должно совпадать");
    }

    @Test
    @DisplayName("Should update task status")
    void shouldUpdateTaskStatus() {
        int taskId = manager.createTask(task);
        // Исправленный конструктор - сначала id, затем остальные параметры
        Task updatedTask = new Task(taskId, "Updated", "Updated", Status.DONE);
        manager.updateTask(updatedTask);
        assertEquals(Status.DONE, manager.getTaskById(taskId).getStatus(),
                "Статус задачи должен обновиться на DONE");
    }
}