package ru.yandex.javacourse.praktikum.service;

import org.junit.jupiter.api.*;
import ru.yandex.javacourse.praktikum.model.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void shouldCreateAndRetrieveTask() {
        Task task = new Task("Test Task", "Description");
        int taskId = taskManager.createTask(task);

        Task retrieved = taskManager.getTaskById(taskId);
        assertNotNull(retrieved);
        assertEquals("Test Task", retrieved.getName());
        assertEquals("Description", retrieved.getDescription());
        assertEquals(Status.NEW, retrieved.getStatus());
    }

    @Test
    void shouldCreateAndRetrieveEpic() {
        Epic epic = new Epic("Test Epic", "Description");
        int epicId = taskManager.createEpic(epic);

        Epic retrieved = taskManager.getEpicById(epicId);
        assertNotNull(retrieved);
        assertEquals("Test Epic", retrieved.getName());
        assertEquals(Status.NEW, retrieved.getStatus());
    }

    @Test
    void shouldCreateAndRetrieveSubtask() {
        Epic epic = new Epic("Test Epic", "Description");
        int epicId = taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Test Subtask", "Description", epicId);
        int subtaskId = taskManager.createSubtask(subtask);

        Subtask retrieved = taskManager.getSubtaskById(subtaskId);
        assertNotNull(retrieved);
        assertEquals("Test Subtask", retrieved.getName());
        assertEquals(epicId, retrieved.getEpicId());
    }

    @Test
    void shouldNotAllowSubtaskWithoutEpic() {
        Subtask subtask = new Subtask("Test Subtask", "Description", 999);

        assertThrows(IllegalArgumentException.class, () -> {
            taskManager.createSubtask(subtask);
        }, "Должно быть выброшено исключение при создании подзадачи без эпика");
    }

    @Test
    void shouldUpdateTaskStatus() {
        Task task = new Task("Test Task", "Description");
        int taskId = taskManager.createTask(task);

        Task updated = new Task(taskId, "Updated", "Updated", Status.IN_PROGRESS);
        taskManager.updateTask(updated);

        Task retrieved = taskManager.getTaskById(taskId);
        assertEquals(Status.IN_PROGRESS, retrieved.getStatus());
    }

    @Test
    void shouldUpdateEpicStatusBasedOnSubtasks() {
        Epic epic = new Epic("Test Epic", "Description");
        int epicId = taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description", epicId);
        int subtask1Id = taskManager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Description", epicId);
        int subtask2Id = taskManager.createSubtask(subtask2);

        // Все NEW -> Epic NEW
        assertEquals(Status.NEW, taskManager.getEpicById(epicId).getStatus());

        // Один IN_PROGRESS -> Epic IN_PROGRESS
        Subtask updated1 = new Subtask(subtask1Id, "Updated", "Updated", Status.IN_PROGRESS, epicId);
        taskManager.updateSubtask(updated1);
        assertEquals(Status.IN_PROGRESS, taskManager.getEpicById(epicId).getStatus());

        // Все DONE -> Epic DONE
        Subtask updated2 = new Subtask(subtask2Id, "Updated", "Updated", Status.DONE, epicId);
        taskManager.updateSubtask(updated2);
        assertEquals(Status.IN_PROGRESS, taskManager.getEpicById(epicId).getStatus());

        // Первый тоже DONE -> Epic DONE
        Subtask updated1Done = new Subtask(subtask1Id, "Updated", "Updated", Status.DONE, epicId);
        taskManager.updateSubtask(updated1Done);
        assertEquals(Status.DONE, taskManager.getEpicById(epicId).getStatus());
    }

    @Test
    void shouldDeleteTask() {
        Task task = new Task("Test Task", "Description");
        int taskId = taskManager.createTask(task);

        taskManager.deleteTaskById(taskId);
        assertNull(taskManager.getTaskById(taskId));
    }

    @Test
    void shouldDeleteEpicWithSubtasks() {
        Epic epic = new Epic("Test Epic", "Description");
        int epicId = taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Test Subtask", "Description", epicId);
        int subtaskId = taskManager.createSubtask(subtask);

        taskManager.deleteEpicById(epicId);

        assertNull(taskManager.getEpicById(epicId));
        assertNull(taskManager.getSubtaskById(subtaskId));
    }

    @Test
    void shouldTrackHistory() {
        Task task = new Task("Task", "Description");
        int taskId = taskManager.createTask(task);

        Epic epic = new Epic("Epic", "Description");
        int epicId = taskManager.createEpic(epic);

        taskManager.getTaskById(taskId);
        taskManager.getEpicById(epicId);

        List<Task> history = taskManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(taskId, history.get(0).getId());
        assertEquals(epicId, history.get(1).getId());
    }
}