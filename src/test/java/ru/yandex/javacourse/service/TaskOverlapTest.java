package ru.yandex.javacourse.service;

import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.exception.ManagerSaveException;
import ru.yandex.javacourse.model.Status;
import ru.yandex.javacourse.model.Task;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskOverlapTest {

    @Test
    void testTasksDoNotOverlap() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        LocalDateTime now = LocalDateTime.now();

        Task task1 = new Task("Task 1", "Description", Status.NEW, now, Duration.ofHours(1));
        manager.createTask(task1);

        Task task2 = new Task("Task 2", "Description", Status.NEW,
                now.plusHours(2), Duration.ofHours(1));

        assertDoesNotThrow(() -> manager.createTask(task2));
    }

    @Test
    void testTasksOverlap() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        LocalDateTime now = LocalDateTime.now();

        Task task1 = new Task("Task 1", "Description", Status.NEW, now, Duration.ofHours(2));
        manager.createTask(task1);

        Task task2 = new Task("Task 2", "Description", Status.NEW,
                now.plusHours(1), Duration.ofHours(1));

        assertThrows(ManagerSaveException.class, () -> manager.createTask(task2));
    }

    @Test
    void testTaskOverlapsWithItselfOnUpdate() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        LocalDateTime now = LocalDateTime.now();

        Task task = new Task("Task", "Description", Status.NEW, now, Duration.ofHours(1));
        int taskId = manager.createTask(task);

        Task updatedTask = manager.getTaskById(taskId);
        updatedTask.setStartTime(now.plusMinutes(30));

        assertDoesNotThrow(() -> manager.updateTask(updatedTask));
    }
}