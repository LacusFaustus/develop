package ru.yandex.javacourse.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private Task task;
    private LocalDateTime testTime;

    @BeforeEach
    void setUp() {
        testTime = LocalDateTime.now();
        task = new Task("Task", "Description", Status.NEW, testTime, Duration.ofMinutes(30));
    }

    @Test
    void tasksWithSameIdShouldBeEqual() {
        Task task1 = new Task(1, "Task 1", "Description", Status.NEW);
        Task task2 = new Task(1, "Task 1", "Description", Status.NEW);
        assertEquals(task1, task2);
    }

    @Test
    void newTaskShouldHaveNewStatus() {
        assertEquals(Status.NEW, task.getStatus());
    }

    @Test
    void shouldUpdateTaskStatus() {
        task.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, task.getStatus());
    }

    @Test
    void shouldCalculateEndTime() {
        LocalDateTime expectedEnd = testTime.plusMinutes(30);
        assertEquals(expectedEnd, task.getEndTime());
    }
}