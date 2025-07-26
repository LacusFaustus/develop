package ru.yandex.javacourse.service;

import org.junit.jupiter.api.*;
import ru.yandex.javacourse.model.Epic;
import ru.yandex.javacourse.model.Subtask;
import ru.yandex.javacourse.model.Task;
import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {
    private TaskManager manager;
    private Task task;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
        task = new Task("Original", "Description");
    }

    @Test
    @DisplayName("Изменения задачи не должны влиять на менеджер")
    void taskChangesShouldNotAffectManager() {
        // Создаем задачу
        Task task = new Task("Original", "Description");
        int taskId = manager.createTask(task);

        // Получаем копию
        Task saved = manager.getTaskById(taskId);
        saved.setName("Modified");

        // Проверяем оригинал
        Task original = manager.getTaskById(taskId);
        assertEquals("Original", original.getName());
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