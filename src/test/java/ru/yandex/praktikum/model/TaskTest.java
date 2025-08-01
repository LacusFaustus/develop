// src/test/java/ru/yandex/praktikum/model/TaskTest.java
package ru.yandex.praktikum.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private Task task;
    private Task anotherTask;

    @BeforeEach
    void setUp() {
        task = new Task("Task", "Description");
        anotherTask = new Task("Another", "Description");
    }

    @Test
    void tasksWithSameIdShouldBeEqual() {
        Task task1 = new Task(1, "Task 1", "Description", Status.NEW);
        Task task2 = new Task(1, "Task 1", "Description", Status.NEW);
        assertEquals(task1, task2);
    }

    @Test
    @DisplayName("New task should have NEW status")
    void newTaskShouldHaveNewStatus() {
        assertEquals(Status.NEW, task.getStatus());
    }

    @Test
    @DisplayName("Should update task status")
    void shouldUpdateTaskStatus() {
        task.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, task.getStatus());
    }

    @Test
    @DisplayName("Should access task fields")
    void shouldAccessTaskFields() {
        assertEquals("Task", task.getName());
        assertEquals("Description", task.getDescription());
    }
}