package ru.yandex.javacourse.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.model.*;
import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {
    private InMemoryTaskManager manager;
    private Task task;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
        task = new Task("Original", "Description");
    }

    @Test
    void taskChangesShouldNotAffectManager() {
        Task task = new Task("Test task", "Description");
        int taskId = manager.createTask(task);
        Task saved = manager.getTaskById(taskId);
        saved.setName("Modified");

        Task fromManager = manager.getTaskById(taskId);
        assertEquals("Test task", fromManager.getName());
    }

    @Test
    void shouldRemoveSubtaskFromEpic() {
        Epic epic = new Epic("Epic", "Description");
        int epicId = manager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask", "Description", epicId);
        int subtaskId = manager.createSubtask(subtask);
        manager.deleteSubtaskById(subtaskId);
        assertFalse(manager.getEpicById(epicId).getSubtaskIds().contains(subtaskId));
    }
}