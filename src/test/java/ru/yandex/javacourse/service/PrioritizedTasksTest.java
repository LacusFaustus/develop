package ru.yandex.javacourse.service;

import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.model.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PrioritizedTasksTest {
    private InMemoryTaskManager manager = new InMemoryTaskManager();

    @Test
    void testTasksAreOrderedByStartTime() {
        Task task1 = new Task("Task 1", "Description", Status.NEW,
                LocalDateTime.now().plusHours(3), Duration.ofHours(1));
        manager.createTask(task1);

        Task task2 = new Task("Task 2", "Description", Status.NEW,
                LocalDateTime.now().plusHours(1), Duration.ofHours(1));
        manager.createTask(task2);

        List<Task> prioritized = manager.getPrioritizedTasks();
        assertEquals(task2.getId(), prioritized.get(0).getId());
        assertEquals(task1.getId(), prioritized.get(1).getId());
    }

    @Test
    void testTasksWithoutTimeAreNotInPrioritizedList() {
        Task task1 = new Task("Task 1", "Description", Status.NEW, null, null);
        manager.createTask(task1);

        Task task2 = new Task("Task 2", "Description", Status.NEW,
                LocalDateTime.now(), Duration.ofHours(1));
        manager.createTask(task2);

        List<Task> prioritized = manager.getPrioritizedTasks();
        assertEquals(1, prioritized.size());
        assertEquals(task2.getId(), prioritized.get(0).getId());
    }
}