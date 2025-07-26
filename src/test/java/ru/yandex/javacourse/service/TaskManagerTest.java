package ru.yandex.javacourse.service;

import org.junit.jupiter.api.*;
import ru.yandex.javacourse.model.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {
    private ru.yandex.javacourse.service.TaskManager manager;
    private Task task;

    @BeforeEach
    void setUp() {
        manager = new ru.yandex.javacourse.service.InMemoryTaskManager();
        task = new Task("Original", "Description");
    }

    @Test
    @DisplayName("Изменения задачи не должны влиять на менеджер")
    void taskChangesShouldNotAffectManager() {
        Task task = new Task("Test task", "Description"); // Создаем новую задачу
        int taskId = manager.createTask(task); // Сохраняем в менеджере
        Task saved = manager.getTaskById(taskId); // Получаем копию
        saved.setName("Modified"); // Изменяем копию

        Task fromManager = manager.getTaskById(taskId); // Получаем новую копию
        assertEquals("Test task", fromManager.getName()); // Проверяем, что оригинал не изменился
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

    @Test
    @DisplayName("История не должна содержать дубликатов")
    void historyShouldNotContainDuplicates() {
        int taskId = manager.createTask(task);
        manager.getTaskById(taskId);
        manager.getTaskById(taskId);
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    @DisplayName("Удаление задачи должно удалять её из истории")
    void deletingTaskShouldRemoveFromHistory() {
        int taskId = manager.createTask(task);
        manager.getTaskById(taskId);
        manager.deleteTaskById(taskId);
        assertTrue(manager.getHistory().isEmpty());
    }
}