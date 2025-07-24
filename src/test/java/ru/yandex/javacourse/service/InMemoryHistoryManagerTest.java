package ru.yandex.javacourse.service;

import org.junit.jupiter.api.*;
import ru.yandex.javacourse.model.Task;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование InMemoryHistoryManager")
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
        assertHistoryEquals(List.of(task1));
    }

    @Test
    @DisplayName("Дубликат задачи перемещается в конец")
    void shouldMoveDuplicateToEnd() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);
        assertHistoryEquals(List.of(task2, task1));
    }

    @Test
    @DisplayName("Удаление задачи из истории")
    void shouldRemoveTaskFromHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task1.getId());
        assertHistoryEquals(List.of(task2));
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
        assertHistoryEquals(List.of(task1));
    }

    private void assertHistoryEquals(List<Task> expected) {
        List<Task> actual = historyManager.getHistory();
        assertEquals(expected, actual, "История не соответствует ожидаемой");
    }
}