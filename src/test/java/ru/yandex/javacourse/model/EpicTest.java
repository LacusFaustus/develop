package ru.yandex.javacourse.model;

import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private Epic epic;
    private Subtask subtask1, subtask2;
    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task("Test task", "Description", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));
        epic = new Epic("Test epic", "Test description", task);
        subtask1 = new Subtask("Subtask 1", "Desc", 1, task);
        subtask2 = new Subtask("Subtask 2", "Desc", 1, task);
    }

    @Test
    @DisplayName("Should update epic status correctly")
    void shouldUpdateEpicStatusCorrectly() {
        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.NEW);
        List<Subtask> subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);
        epic.updateStatus(subtasks);
        assertEquals(Status.NEW, epic.getStatus());
    }
}