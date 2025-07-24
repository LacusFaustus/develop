package ru.yandex.javacourse.service;

import org.junit.jupiter.api.*;
import ru.yandex.javacourse.model.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {
    private TaskManager manager;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
    }

    @Test
    @DisplayName("Изменения задачи не влияют на менеджер")
    void taskChangesShouldNotAffectManager() {
        Task task = new Task("Original", "Description");
        int taskId = manager.createTask(task);
        task.setName("Modified");
        Task saved = manager.getTaskById(taskId);
        assertEquals("Original", saved.getName());
    }

    @Test
    @DisplayName("Удаление подзадачи из эпика")
    void shouldRemoveSubtaskFromEpic() {
        Epic epic = new Epic("Epic", "Description");
        int epicId = manager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask", "Description", epicId);
        int subtaskId = manager.createSubtask(subtask);
        manager.deleteSubtaskById(subtaskId);
        assertFalse(manager.getEpicById(epicId).getSubtaskIds().contains(subtaskId));
    }
}