// src/test/java/ru/yandex/praktikum/service/TaskManagerTest.java
package ru.yandex.praktikum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.model.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    // Тестовые данные
    protected Task task;
    protected Epic epic;
    protected Subtask subtask1;
    protected Subtask subtask2;

    @BeforeEach
    void setUp() {
        initManager();
        createTestData();
    }

    // Абстрактный метод для инициализации менеджера
    protected abstract void initManager();

    // Создание тестовых данных
    private void createTestData() {
        task = new Task("Test Task", "Description");
        int taskId = taskManager.createTask(task);
        task.setId(taskId);

        epic = new Epic("Test Epic", "Description");
        int epicId = taskManager.createEpic(epic);
        epic.setId(epicId);

        subtask1 = new Subtask("Subtask 1", "Description", epicId);
        int subtask1Id = taskManager.createSubtask(subtask1);
        subtask1.setId(subtask1Id);

        subtask2 = new Subtask("Subtask 2", "Description", epicId);
        int subtask2Id = taskManager.createSubtask(subtask2);
        subtask2.setId(subtask2Id);
    }

    @Test
    void shouldCreateAndRetrieveTask() {
        Task retrieved = taskManager.getTaskById(task.getId());
        assertNotNull(retrieved, "Задача должна быть найдена");
        assertEquals(task.getName(), retrieved.getName(), "Название задачи должно совпадать");
        assertEquals(task.getDescription(), retrieved.getDescription(), "Описание задачи должно совпадать");
        assertEquals(Status.NEW, retrieved.getStatus(), "Статус задачи должен быть NEW");
    }

    @Test
    void shouldCreateAndRetrieveEpic() {
        Epic retrieved = taskManager.getEpicById(epic.getId());
        assertNotNull(retrieved, "Эпик должен быть найден");
        assertEquals(epic.getName(), retrieved.getName(), "Название эпика должно совпадать");
        assertEquals(2, retrieved.getSubtaskIds().size(), "Эпик должен содержать 2 подзадачи");
        assertTrue(retrieved.getSubtaskIds().contains(subtask1.getId()), "Эпик должен содержать подзадачу 1");
        assertTrue(retrieved.getSubtaskIds().contains(subtask2.getId()), "Эпик должен содержать подзадачу 2");
    }

    @Test
    void shouldCreateAndRetrieveSubtask() {
        Subtask retrieved = taskManager.getSubtaskById(subtask1.getId());
        assertNotNull(retrieved, "Подзадача должна быть найдена");
        assertEquals(subtask1.getName(), retrieved.getName(), "Название подзадачи должно совпадать");
        assertEquals(epic.getId(), retrieved.getEpicId(), "ID эпика должно совпадать");
    }

    @Test
    void shouldNotAllowSubtaskWithoutEpic() {
        Subtask invalidSubtask = new Subtask("Invalid", "Description", 999);
        assertThrows(IllegalArgumentException.class, () -> {
            taskManager.createSubtask(invalidSubtask);
        }, "Должно быть выброшено исключение при создании подзадачи без эпика");
    }

    @Test
    void shouldUpdateTask() {
        Task updated = new Task(task.getId(), "Updated Task", "Updated Description", Status.IN_PROGRESS);
        taskManager.updateTask(updated);

        Task retrieved = taskManager.getTaskById(task.getId());
        assertEquals("Updated Task", retrieved.getName(), "Название задачи должно обновиться");
        assertEquals("Updated Description", retrieved.getDescription(), "Описание задачи должно обновиться");
        assertEquals(Status.IN_PROGRESS, retrieved.getStatus(), "Статус задачи должен обновиться");
    }

    @Test
    void shouldUpdateEpic() {
        Epic updated = new Epic(epic.getId(), "Updated Epic", "Updated Description", Status.DONE);
        taskManager.updateEpic(updated);

        Epic retrieved = taskManager.getEpicById(epic.getId());
        assertEquals("Updated Epic", retrieved.getName(), "Название эпика должно обновиться");
        assertEquals("Updated Description", retrieved.getDescription(), "Описание эпика должно обновиться");
        // Статус эпика не должен меняться при прямом обновлении
        assertNotEquals(Status.DONE, retrieved.getStatus(), "Статус эпика не должен меняться при прямом обновлении");
    }

    @Test
    void shouldUpdateSubtaskAndEpicStatus() {
        // Изменяем статус подзадачи
        Subtask updated = new Subtask(subtask1.getId(), "Updated", "Updated", Status.DONE, epic.getId());
        taskManager.updateSubtask(updated);

        // Проверяем подзадачу
        Subtask retrievedSubtask = taskManager.getSubtaskById(subtask1.getId());
        assertEquals(Status.DONE, retrievedSubtask.getStatus(), "Статус подзадачи должен обновиться");

        // Проверяем статус эпика
        Epic retrievedEpic = taskManager.getEpicById(epic.getId());
        assertEquals(Status.IN_PROGRESS, retrievedEpic.getStatus(), "Статус эпика должен быть IN_PROGRESS");

        // Изменяем вторую подзадачу
        Subtask updated2 = new Subtask(subtask2.getId(), "Updated", "Updated", Status.DONE, epic.getId());
        taskManager.updateSubtask(updated2);

        // Проверяем статус эпика после обеих подзадач
        retrievedEpic = taskManager.getEpicById(epic.getId());
        assertEquals(Status.DONE, retrievedEpic.getStatus(), "Статус эпика должен быть DONE");
    }

    @Test
    void shouldDeleteTask() {
        taskManager.deleteTaskById(task.getId());
        assertNull(taskManager.getTaskById(task.getId()), "Задача должна быть удалена");
        assertEquals(0, taskManager.getHistory().size(), "История должна быть пустой");
    }

    @Test
    void shouldDeleteEpicWithSubtasks() {
        taskManager.deleteEpicById(epic.getId());

        assertNull(taskManager.getEpicById(epic.getId()), "Эпик должен быть удален");
        assertNull(taskManager.getSubtaskById(subtask1.getId()), "Подзадача 1 должна быть удалена");
        assertNull(taskManager.getSubtaskById(subtask2.getId()), "Подзадача 2 должна быть удалена");
        assertEquals(0, taskManager.getHistory().size(), "История должна быть пустой");
    }

    @Test
    void shouldDeleteSubtask() {
        taskManager.deleteSubtaskById(subtask1.getId());

        assertNull(taskManager.getSubtaskById(subtask1.getId()), "Подзадача должна быть удалена");

        Epic retrievedEpic = taskManager.getEpicById(epic.getId());
        assertFalse(retrievedEpic.getSubtaskIds().contains(subtask1.getId()), "Эпик не должен содержать удаленную подзадачу");
        assertEquals(1, retrievedEpic.getSubtaskIds().size(), "Эпик должен содержать одну подзадачу");
    }

    @Test
    void shouldGetSubtasksByEpic() {
        List<Subtask> subtasks = taskManager.getSubtasksByEpicId(epic.getId());

        assertEquals(2, subtasks.size(), "Должно быть 2 подзадачи");
        assertTrue(subtasks.stream().anyMatch(s -> s.getId() == subtask1.getId()), "Должна быть подзадача 1");
        assertTrue(subtasks.stream().anyMatch(s -> s.getId() == subtask2.getId()), "Должна быть подзадача 2");
    }

    @Test
    void shouldTrackHistory() {
        // Получаем задачи в определенном порядке
        taskManager.getTaskById(task.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.getSubtaskById(subtask1.getId());

        List<Task> history = taskManager.getHistory();
        assertEquals(3, history.size(), "История должна содержать 3 элемента");
        assertEquals(task.getId(), history.get(0).getId(), "Первой должна быть задача");
        assertEquals(epic.getId(), history.get(1).getId(), "Вторым должен быть эпик");
        assertEquals(subtask1.getId(), history.get(2).getId(), "Третьей должна быть подзадача");

        // Повторное получение должно изменить порядок
        taskManager.getEpicById(epic.getId());
        history = taskManager.getHistory();
        assertEquals(3, history.size(), "История должна содержать 3 элемента");
        assertEquals(task.getId(), history.get(0).getId(), "Первой должна быть задача");
        assertEquals(subtask1.getId(), history.get(1).getId(), "Второй должна быть подзадача");
        assertEquals(epic.getId(), history.get(2).getId(), "Последним должен быть эпик");
    }

    @Test
    void shouldHandleTaskChangesWithoutAffectingManager() {
        // Получаем задачу и изменяем ее
        Task retrieved = taskManager.getTaskById(task.getId());
        retrieved.setName("Modified");
        retrieved.setStatus(Status.DONE);

        // Проверяем, что в менеджере задача не изменилась
        Task shouldBeOriginal = taskManager.getTaskById(task.getId());
        assertEquals("Test Task", shouldBeOriginal.getName(), "Название задачи не должно измениться");
        assertEquals(Status.NEW, shouldBeOriginal.getStatus(), "Статус задачи не должен измениться");
    }

    @Test
    void shouldHandleEpicChangesWithoutAffectingManager() {
        // Получаем эпик и изменяем его
        Epic retrieved = taskManager.getEpicById(epic.getId());
        retrieved.setName("Modified");
        retrieved.setStatus(Status.DONE);

        // Проверяем, что в менеджере эпик не изменился
        Epic shouldBeOriginal = taskManager.getEpicById(epic.getId());
        assertEquals("Test Epic", shouldBeOriginal.getName(), "Название эпика не должно измениться");
        assertEquals(Status.NEW, shouldBeOriginal.getStatus(), "Статус эпика не должен измениться");
    }

    @Test
    void shouldHandleSubtaskChangesWithoutAffectingManager() {
        // Получаем подзадачу и изменяем ее
        Subtask retrieved = taskManager.getSubtaskById(subtask1.getId());
        retrieved.setName("Modified");
        retrieved.setStatus(Status.DONE);

        // Проверяем, что в менеджере подзадача не изменилась
        Subtask shouldBeOriginal = taskManager.getSubtaskById(subtask1.getId());
        assertEquals("Subtask 1", shouldBeOriginal.getName(), "Название подзадачи не должно измениться");
        assertEquals(Status.NEW, shouldBeOriginal.getStatus(), "Статус подзадачи не должен измениться");
    }

    @Test
    void shouldNotAddToHistoryWhenTaskNotFound() {
        // Попытка получить несуществующую задачу
        assertNull(taskManager.getTaskById(999), "Должен вернуться null");
        assertEquals(0, taskManager.getHistory().size(), "История должна остаться пустой");
    }
}