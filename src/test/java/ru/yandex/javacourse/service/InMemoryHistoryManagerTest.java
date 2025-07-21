import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.model.Task;
import ru.yandex.javacourse.service.HistoryManager;
import ru.yandex.javacourse.util.InMemoryHistoryManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task("Task 1", "Description 1");
        task2 = new Task("Task 2", "Description 2");
        task3 = new Task("Task 3", "Description 3");
    }

    @Test
    void addShouldAddTaskToHistory() {
        historyManager.add(task1);
        assertEquals(List.of(task1), historyManager.getHistory());
    }

    @Test
    void addShouldRemoveDuplicateAndAddToEnd() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);

        assertEquals(List.of(task2, task1), historyManager.getHistory());
    }

    @Test
    void removeShouldRemoveTaskFromHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task1.getId());

        assertEquals(List.of(task2), historyManager.getHistory());
    }

    @Test
    void getHistoryShouldReturnEmptyListForEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void removeShouldWorkCorrectlyWhenRemovingHead() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task1.getId());

        assertEquals(List.of(task2), historyManager.getHistory());
    }

    @Test
    void removeShouldWorkCorrectlyWhenRemovingTail() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task2.getId());

        assertEquals(List.of(task1), historyManager.getHistory());
    }

    @Test
    void removeShouldWorkCorrectlyWhenRemovingMiddleNode() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task2.getId());

        assertEquals(List.of(task1, task3), historyManager.getHistory());
    }
}