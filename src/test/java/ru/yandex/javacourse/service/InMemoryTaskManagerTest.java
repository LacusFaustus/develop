package ru.yandex.javacourse.service;

import org.junit.jupiter.api.*;
import ru.yandex.javacourse.model.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager manager;
    private Task task;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
        task = new Task("Task", "Description");
        epic = new Epic("Epic", "Description");
        manager.createEpic(epic);
        subtask = new Subtask("Subtask", "Description", epic.getId());
    }

    @Test
    @DisplayName("Создание и получение задачи")
    void shouldCreateAndRetrieveTask() {
        int taskId = manager.createTask(task);
        Task saved = manager.getTaskById(taskId);
        assertEquals(task.getName(), saved.getName());
        assertEquals(task.getDescription(), saved.getDescription());
    }

    @Test
    @DisplayName("Связь подзадачи с эпиком")
    void shouldLinkSubtaskToEpic() {
        int subtaskId = manager.createSubtask(subtask);
        List<Subtask> subtasks = manager.getSubtasksByEpicId(epic.getId());
        assertEquals(1, subtasks.size());
        assertEquals(subtaskId, subtasks.get(0).getId());
    }

    @Test
    @DisplayName("Обновление задачи")
    void shouldUpdateTask() {
        int taskId = manager.createTask(task);
        Task updated = new Task(taskId, "Updated", "Updated", Status.DONE);
        manager.updateTask(updated);
        Task saved = manager.getTaskById(taskId);
        assertEquals("Updated", saved.getName());
        assertEquals("Updated", saved.getDescription());
        assertEquals(Status.DONE, saved.getStatus());
    }

    @Test
    @DisplayName("Удаление эпика с подзадачами")
    void shouldDeleteEpicWithSubtasks() {
        manager.createSubtask(subtask);
        manager.deleteEpicById(epic.getId());
        assertEquals(0, manager.getAllEpics().size());
        assertEquals(0, manager.getAllSubtasks().size());
    }

    @Test
    @DisplayName("Попытка создания null задачи")
    void shouldThrowWhenCreatingNullTask() {
        assertThrows(IllegalArgumentException.class, () -> manager.createTask(null));
    }

}