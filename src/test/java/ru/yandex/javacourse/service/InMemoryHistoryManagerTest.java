package ru.yandex.javacourse.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.model.Status;
import ru.yandex.javacourse.model.Task;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;
    private Task task1, task2, task3;
    private final LocalDateTime testTime = LocalDateTime.now();
    private final Duration testDuration = Duration.ofMinutes(30);

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task("Task 1", "Description 1", Status.NEW, testTime, testDuration);
        task1.setId(1);
        task2 = new Task("Task 2", "Description 2", Status.NEW, testTime, testDuration);
        task2.setId(2);
        task3 = new Task("Task 3", "Description 3", Status.NEW, testTime, testDuration);
        task3.setId(3);
    }

    @Test
    void shouldAddTaskToHistory() {
        historyManager.add(task1);
        List<Task> expected = List.of(task1);
        assertIterableEquals(expected, historyManager.getHistory());
    }

    @Test
    void shouldMoveDuplicateToEnd() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);
        List<Task> expected = List.of(task2, task1);
        assertIterableEquals(expected, historyManager.getHistory());
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task1.getId());
        List<Task> expected = List.of(task2);
        assertIterableEquals(expected, historyManager.getHistory());
    }

    @Test
    void newManagerShouldHaveEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void shouldIgnoreNonExistentTaskRemoval() {
        historyManager.add(task1);
        historyManager.remove(999);
        List<Task> expected = List.of(task1);
        assertIterableEquals(expected, historyManager.getHistory());
    }

    @Test
    void shouldRemoveFromBeginning() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task1.getId());
        assertEquals(List.of(task2, task3), historyManager.getHistory());
    }

    @Test
    void shouldRemoveFromMiddle() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task2.getId());
        assertEquals(List.of(task1, task3), historyManager.getHistory());
    }

    @Test
    void shouldRemoveFromEnd() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task3.getId());
        assertEquals(List.of(task1, task2), historyManager.getHistory());
    }
}