package ru.yandex.javacourse.service;

import org.junit.jupiter.api.*;
import ru.yandex.javacourse.model.Task;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;
    private Task task1, task2, task3;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task("Task 1", "Description 1");
        task1.setId(1);
        task2 = new Task("Task 2", "Description 2");
        task2.setId(2);
        task3 = new Task("Task 3", "Description 3");
        task3.setId(3);
    }

    @Test
    @DisplayName("Добавление задачи в историю")
    void shouldAddTaskToHistory() {
        historyManager.add(task1);
        List<Task> expected = new ArrayList<>();
        expected.add(task1);
        assertHistoryEquals(expected);
    }

    @Test
    @DisplayName("Дубликат задачи перемещается в конец")
    void shouldMoveDuplicateToEnd() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);
        List<Task> expected = new ArrayList<>();
        expected.add(task2);
        expected.add(task1);
        assertHistoryEquals(expected);
    }

    @Test
    @DisplayName("Удаление задачи из истории")
    void shouldRemoveTaskFromHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task1.getId());
        List<Task> expected = new ArrayList<>();
        expected.add(task2);
        assertHistoryEquals(expected);
    }

    @Test
    @DisplayName("Новая история должна быть пустой")
    void newManagerShouldHaveEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    @DisplayName("Удаление несуществующей задачи")
    void shouldIgnoreNonExistentTaskRemoval() {
        historyManager.add(task1);
        historyManager.remove(999);
        List<Task> expected = new ArrayList<>();
        expected.add(task1);
        assertHistoryEquals(expected);
    }

    @Test
    @DisplayName("Удаление задачи из середины истории")
    void shouldRemoveTaskFromMiddle() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task2.getId());

        List<Task> expected = List.of(task1, task3);
        assertHistoryEquals(expected);
    }

    @Test
    @DisplayName("История не должна содержать дубликатов")
    void shouldNotContainDuplicates() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);
        historyManager.add(task3);
        historyManager.add(task2);

        List<Task> expected = List.of(task1, task3, task2);
        assertHistoryEquals(expected);
    }

    @Test
    @DisplayName("Удаление из пустой истории")
    void shouldHandleEmptyHistoryRemoval() {
        historyManager.remove(1);
        assertTrue(historyManager.getHistory().isEmpty());
    }


    private void assertHistoryEquals(List<Task> expected) {
        List<Task> actual = historyManager.getHistory();
        assertEquals(expected, actual, "История не соответствует ожидаемой");
    }
}