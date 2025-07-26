package ru.yandex.javacourse.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.model.*;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager manager;
    private Task task;
    private Epic epic;
    private Subtask subtask;
    private int taskId;
    private int epicId;
    private int subtaskId;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();

        // Создаем тестовые задачи
        task = new Task("Test task", "Test description");
        taskId = manager.createTask(task);

        epic = new Epic("Test epic", "Test description");
        epicId = manager.createEpic(epic);

        subtask = new Subtask("Test subtask", "Test description", epicId);
        subtaskId = manager.createSubtask(subtask);
    }

    @Test
    void shouldCreateAndRetrieveTask() {
        Task retrieved = manager.getTaskById(taskId);

        assertNotNull(retrieved, "Задача не найдена");
        assertEquals(taskId, retrieved.getId(), "ID задачи не совпадает");
        assertEquals(task.getName(), retrieved.getName(), "Название задачи не совпадает");
        assertEquals(task.getDescription(), retrieved.getDescription(), "Описание задачи не совпадает");
        assertEquals(Status.NEW, retrieved.getStatus(), "Статус задачи не совпадает");
    }

    @Test
    void shouldCreateAndRetrieveEpic() {
        Epic retrieved = manager.getEpicById(epicId);

        assertNotNull(retrieved, "Эпик не найден");
        assertEquals(epicId, retrieved.getId(), "ID эпика не совпадает");
        assertEquals(1, retrieved.getSubtaskIds().size(), "Неверное количество подзадач в эпике");
        assertTrue(retrieved.getSubtaskIds().contains(subtaskId), "Подзадача не найдена в эпике");
    }

    @Test
    void shouldCreateAndRetrieveSubtask() {
        Subtask retrieved = manager.getSubtaskById(subtaskId);

        assertNotNull(retrieved, "Подзадача не найдена");
        assertEquals(subtaskId, retrieved.getId(), "ID подзадачи не совпадает");
        assertEquals(epicId, retrieved.getEpicId(), "ID эпика подзадачи не совпадает");
        assertEquals(Status.NEW, retrieved.getStatus(), "Статус подзадачи не совпадает");
    }

    @Test
    void shouldUpdateTaskStatus() {
        // Подготовка
        Task updatedTask = new Task(taskId, "Updated", "Updated", Status.DONE);

        // Действие
        manager.updateTask(updatedTask);

        // Проверка
        assertEquals(Status.DONE, manager.getTaskById(taskId).getStatus(),
                "Статус задачи не обновился");
    }

    @Test
    void shouldUpdateSubtaskStatusAndEpicStatus() {
        // Подготовка
        Subtask updatedSubtask = new Subtask(subtaskId, "Updated", "Updated", Status.DONE, epicId);

        // Действие
        manager.updateSubtask(updatedSubtask);

        // Проверки
        assertEquals(Status.DONE, manager.getSubtaskById(subtaskId).getStatus(),
                "Статус подзадачи не обновился");
        assertEquals(Status.DONE, manager.getEpicById(epicId).getStatus(),
                "Статус эпика не обновился");
    }

    @Test
    void shouldNotAllowSubtaskWithoutEpic() {
        // Проверка, что нельзя создать подзадачу без существующего эпика
        Subtask invalidSubtask = new Subtask("Invalid", "No epic", 999);

        assertThrows(IllegalArgumentException.class, () -> manager.createSubtask(invalidSubtask),
                "Должно быть выброшено исключение при создании подзадачи без эпика");
    }

    @Test
    void shouldDeleteTask() {
        // Действие
        manager.deleteTaskById(taskId);

        // Проверка
        assertNull(manager.getTaskById(taskId), "Задача не была удалена");
    }

    @Test
    void shouldDeleteEpicWithSubtasks() {
        // Действие
        manager.deleteEpicById(epicId);

        // Проверки
        assertNull(manager.getEpicById(epicId), "Эпик не был удален");
        assertNull(manager.getSubtaskById(subtaskId), "Подзадача не была удалена");
    }
}