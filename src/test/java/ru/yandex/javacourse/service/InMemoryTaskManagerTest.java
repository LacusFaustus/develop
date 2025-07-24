package ru.yandex.javacourse.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import ru.yandex.javacourse.model.Task;
import ru.yandex.javacourse.model.Epic;
import ru.yandex.javacourse.model.Subtask;
import ru.yandex.javacourse.service.TaskManager;  // исправленный импорт

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
    @DisplayName("Должен создавать и возвращать задачу с тем же ID")
    void shouldCreateAndRetrieveTaskWithSameId() {
        int taskId = manager.createTask(task);
        Task retrieved = manager.getTaskById(taskId);
        assertEquals(taskId, retrieved.getId());
    }

    @Test
    @DisplayName("Должен создавать и возвращать подзадачу")
    void shouldCreateAndRetrieveSubtask() {
        int subtaskId = manager.createSubtask(subtask);
        Subtask retrieved = manager.getSubtaskById(subtaskId);
        assertEquals(subtaskId, retrieved.getId());
    }
}