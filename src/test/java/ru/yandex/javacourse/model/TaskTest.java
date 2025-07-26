package ru.yandex.javacourse.model;

<<<<<<< HEAD
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
=======
import ru.yandex.javacourse.model.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import ru.yandex.javacourse.model.Status;
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67

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
        assertEquals(task1, task2, "Задачи с одинаковым id должны быть равны");
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