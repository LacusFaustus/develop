package main.java.ru.yandex.javacourse.service;

import main.java.ru.yandex.javacourse.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest {
    private TaskManager manager;
    private Task task;
    private Subtask subtask;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
        task = new Task("Test task", "Test description");
        Epic epic = new Epic("Test epic", "Test description");
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
        int subtaskId = manager.createSubtask(subtask);
        Subtask retrieved = manager.getSubtaskById(subtaskId);
        assertEquals(subtaskId, retrieved.getId(), "ID созданной подзадачи должно совпадать с полученным ID");
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