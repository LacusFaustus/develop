package test.java.ru.yandex.javacourse.service;

import main.java.ru.yandex.javacourse.model.*;
import main.java.ru.yandex.javacourse.service.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager manager;
    private Task task;
    private Subtask subtask;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
        task = new Task("Test task", "Test description");
        Epic epic = new Epic("Test epic", "Test description");
        int epicId = manager.createEpic(epic);
        subtask = new Subtask("Test subtask", "Test description", epicId);
    }

    @Test
    void shouldCreateAndRetrieveTask() {
        int taskId = manager.createTask(task);
        Task saved = manager.getTaskById(taskId);
        assertEquals(taskId, saved.getId());
    }
}