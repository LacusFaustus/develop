package ru.yandex.javacourse.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private static final String TASK_NAME = "Test Task";
    private static final String TASK_DESCRIPTION = "Test Description";

    private Task task;
    private Task anotherTask;

    @BeforeEach
    void setUp() {
        task = new Task(TASK_NAME, TASK_DESCRIPTION);
        anotherTask = new Task("Another Task", "Another Description");
    }

    @Test
    @DisplayName("Tasks with same ID should be equal")
    void equals_shouldReturnTrue_whenTasksHaveSameId() {
        task.setId(1);
        anotherTask.setId(1);

        assertEquals(task, anotherTask, "Задачи с одинаковым id должны быть равны");
    }

    @Test
    @DisplayName("New task should have NEW status")
    void getStatus_shouldReturnNew_whenTaskCreated() {
        assertEquals(Status.NEW, task.getStatus(), "Новая задача должна иметь статус NEW");
    }

    @Test
    @DisplayName("Task status should be updated correctly")
    void setStatus_shouldUpdateStatus() {
        task.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, task.getStatus(), "Статус должен измениться на IN_PROGRESS");

        task.setStatus(Status.DONE);
        assertEquals(Status.DONE, task.getStatus(), "Статус должен измениться на DONE");
    }

    @Test
    @DisplayName("Task fields should be accessible via getters")
    void getters_shouldReturnCorrectValues() {
        assertEquals(TASK_NAME, task.getName(), "Название задачи должно соответствовать");
        assertEquals(TASK_DESCRIPTION, task.getDescription(), "Описание задачи должно соответствовать");
    }

    @Test
    @DisplayName("Task fields should be modifiable via setters")
    void setters_shouldUpdateValues() {
        String newName = "Updated Task";
        String newDescription = "Updated Description";

        task.setName(newName);
        task.setDescription(newDescription);

        assertEquals(newName, task.getName(), "Название задачи должно обновиться");
        assertEquals(newDescription, task.getDescription(), "Описание задачи должно обновиться");
    }
}