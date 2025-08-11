package ru.yandex.javacourse.service;

import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.model.Status;
import ru.yandex.javacourse.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest extends AbstractTaskManagerTest<InMemoryTaskManager> {

    @Override
    protected void initManager() {
        manager = new InMemoryTaskManager();
    }

    @Test
    void testPrioritizedTasksOrder() {
        // Очищаем менеджер перед тестом
        manager.deleteAllTasks();
        manager.deleteAllSubtasks();

        Task task1 = new Task("Task 1", "Desc", Status.NEW,
                LocalDateTime.now().plusHours(4), Duration.ofHours(1));
        int id1 = manager.createTask(task1);

        Task task2 = new Task("Task 2", "Desc", Status.NEW,
                LocalDateTime.now().plusHours(2), Duration.ofHours(1));
        int id2 = manager.createTask(task2);

        List<Task> prioritized = manager.getPrioritizedTasks();
        assertEquals(2, prioritized.size());
        assertEquals(id2, prioritized.get(0).getId());
        assertEquals(id1, prioritized.get(1).getId());
    }
}