package ru.yandex.javacourse.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.model.Epic;
import ru.yandex.javacourse.model.Status;
import ru.yandex.javacourse.model.Subtask;
import ru.yandex.javacourse.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TaskManagerTest {
    private InMemoryTaskManager manager;
    private LocalDateTime testTime;
    private Duration testDuration;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
        testTime = LocalDateTime.now();
        testDuration = Duration.ofMinutes(30);
    }

    @Test
    void taskChangesShouldNotAffectManager() {
        Task task = new Task("Test task", "Description", Status.NEW, testTime, testDuration);
        int taskId = manager.createTask(task);

        // Создаем копию задачи вместо изменения сохраненной
        Task modifiedTask = new Task(task);
        modifiedTask.setName("Modified");

        Task fromManager = manager.getTaskById(taskId);
        assertEquals("Test task", fromManager.getName());
    }

    @Test
    void shouldRemoveSubtaskFromEpic() {
        Epic epic = new Epic("Epic", "Description");
        int epicId = manager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask", "Description", epicId,
                Status.NEW, testTime, testDuration);
        int subtaskId = manager.createSubtask(subtask);

        manager.deleteSubtaskById(subtaskId);
        assertFalse(manager.getEpicById(epicId).getSubtaskIds().contains(subtaskId));
    }
}