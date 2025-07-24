package ru.yandex.javacourse.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private Task task, anotherTask;

    @BeforeEach
    void setUp() {
        task = new Task("Task", "Description");
        anotherTask = new Task("Another", "Description");
    }

    @Test
    @DisplayName("Равенство задач с одинаковым ID")
    void tasksWithSameIdShouldBeEqual() {
        task.setId(1);
        anotherTask.setId(1);
        assertEquals(task, anotherTask);
    }

    @Test
    @DisplayName("Новая задача имеет статус NEW")
    void newTaskShouldHaveNewStatus() {
        assertEquals(Status.NEW, task.getStatus());
    }

    @Test
    @DisplayName("Изменение статуса задачи")
    void shouldUpdateTaskStatus() {
        task.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, task.getStatus());
    }

    @Test
    @DisplayName("Доступ к полям задачи")
    void shouldAccessTaskFields() {
        assertEquals("Task", task.getName());
        assertEquals("Description", task.getDescription());
    }
}