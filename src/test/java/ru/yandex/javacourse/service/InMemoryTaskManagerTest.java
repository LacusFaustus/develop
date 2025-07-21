package ru.yandex.javacourse.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.model.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private static final String TASK_NAME = "Test Task";
    private static final String TASK_DESCRIPTION = "Test Description";
    private static final String EPIC_NAME = "Test Epic";
    private static final String EPIC_DESCRIPTION = "Test Description";
    private static final String SUBTASK_NAME = "Test Subtask";

    private TaskManager manager;
    private Task task;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
        task = new Task(TASK_NAME, TASK_DESCRIPTION);
        epic = new Epic(EPIC_NAME, EPIC_DESCRIPTION);

        // Сначала создаем эпик, потом подзадачу
        manager.createEpic(epic);
        subtask = new Subtask(SUBTASK_NAME, TASK_DESCRIPTION, epic.getId());
    }

    @Test
    @DisplayName("Task should be created and retrieved correctly")
    void createTask_shouldCreateAndRetrieveTask() {
        manager.createTask(task);
        Task savedTask = manager.getTaskById(task.getId());

        assertNotNull(savedTask, "Задача должна существовать");
        assertEquals(task, savedTask, "Задачи должны быть равны");
        assertEquals(1, manager.getAllTasks().size(), "Должна быть одна задача");
    }

    @Test
    @DisplayName("Epic should be created and retrieved correctly")
    void createEpic_shouldCreateAndRetrieveEpic() {
        Epic newEpic = new Epic("New Epic", "New Description");
        manager.createEpic(newEpic);
        Epic savedEpic = manager.getEpicById(newEpic.getId());

        assertNotNull(savedEpic, "Эпик должен существовать");
        assertEquals(newEpic, savedEpic, "Эпики должны быть равны");
        assertEquals(Status.NEW, savedEpic.getStatus(), "Статус нового эпика должен быть NEW");
    }

    @Test
    @DisplayName("Subtask should be created and linked to Epic")
    void createSubtask_shouldCreateAndLinkToEpic() {
        manager.createSubtask(subtask);

        List<Subtask> subtasks = manager.getSubtasksByEpicId(epic.getId());
        assertEquals(1, subtasks.size(), "Должна быть одна подзадача");
        assertEquals(subtask, subtasks.get(0), "Подзадачи должны быть равны");
    }

    @Test
    @DisplayName("Task update should change task fields")
    void updateTask_shouldChangeTaskFields() {
        manager.createTask(task);
        Task updatedTask = new Task("Updated", "Updated");
        updatedTask.setId(task.getId());
        updatedTask.setStatus(Status.DONE);
        manager.updateTask(updatedTask);

        Task savedTask = manager.getTaskById(task.getId());
        assertEquals("Updated", savedTask.getName());
        assertEquals("Updated", savedTask.getDescription());
        assertEquals(Status.DONE, savedTask.getStatus());
    }

    @Test
    @DisplayName("History should contain viewed tasks")
    void getHistory_shouldReturnViewedTasks() {
        manager.createTask(task);
        manager.getTaskById(task.getId());
        manager.getEpicById(epic.getId());

        List<Task> history = manager.getHistory();
        assertEquals(2, history.size());
        assertTrue(history.contains(task));
        assertTrue(history.contains(epic));
    }

    @Test
    @DisplayName("Deleting epic should delete its subtasks")
    void deleteEpic_shouldDeleteSubtasks() {
        manager.createSubtask(subtask);
        manager.deleteEpicById(epic.getId());

        assertEquals(0, manager.getAllEpics().size());
        assertEquals(0, manager.getAllSubtasks().size());
    }
}