package ru.yandex.javacourse.service;

<<<<<<< HEAD
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.model.Epic;
import ru.yandex.javacourse.model.Subtask;
import ru.yandex.javacourse.model.Task;
import ru.yandex.javacourse.model.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;
=======
import org.junit.jupiter.api.*;
import ru.yandex.javacourse.model.*;
import static org.junit.jupiter.api.Assertions.*;
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67

class InMemoryTaskManagerTest {
    private TaskManager manager;
    private Task task;
<<<<<<< HEAD
=======
    private Epic epic;
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
    private Subtask subtask;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
        task = new Task("Test task", "Test description");
<<<<<<< HEAD
        Epic epic = new Epic("Test epic", "Test description");
=======
        epic = new Epic("Test epic", "Test description");
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
        manager.createEpic(epic);
        subtask = new Subtask("Test subtask", "Test description", epic.getId());
    }

    @Test
    @DisplayName("Should create and return task with same ID")
    void shouldCreateAndRetrieveTaskWithSameId() {
        int taskId = manager.createTask(task);
        Task retrieved = manager.getTaskById(taskId);
        assertEquals(taskId, retrieved.getId(), "ID созданной задачи должно совпадать с полученным ID");
    }

    @Test
    @DisplayName("Should create and return subtask")
    void shouldCreateAndRetrieveSubtask() {
<<<<<<< HEAD
        int subtaskId = manager.createSubtask(subtask);
        Subtask retrieved = manager.getSubtaskById(subtaskId);
        assertEquals(subtaskId, retrieved.getId(), "ID созданной подзадачи должно совпадать с полученным ID");
=======
        // Создаем эпик
        Epic epic = new Epic("Test epic", "Test description");
        int epicId = manager.createEpic(epic);

        // Создаем подзадачу
        Subtask subtask = new Subtask("Test subtask", "Test description", epicId);
        int subtaskId = manager.createSubtask(subtask);

        // Получаем подзадачу
        Subtask retrieved = manager.getSubtaskById(subtaskId);

        // Проверяем
        assertNotNull(retrieved, "Подзадача не должна быть null");
        assertEquals(subtaskId, retrieved.getId(), "ID подзадачи должно совпадать");
        assertEquals(epicId, retrieved.getEpicId(), "Epic ID должно совпадать");
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
    }

    @Test
    @DisplayName("Should update task status")
    void shouldUpdateTaskStatus() {
        int taskId = manager.createTask(task);
<<<<<<< HEAD
        Task updatedTask = new Task(taskId, "Updated", "Updated", Status.DONE); // Исправленный конструктор
=======
        // Исправленный конструктор - сначала id, затем остальные параметры
        Task updatedTask = new Task(taskId, "Updated", "Updated", Status.DONE);
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
        manager.updateTask(updatedTask);
        assertEquals(Status.DONE, manager.getTaskById(taskId).getStatus(),
                "Статус задачи должен обновиться на DONE");
    }
}