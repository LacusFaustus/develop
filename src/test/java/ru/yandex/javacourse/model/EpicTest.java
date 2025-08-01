package ru.yandex.javacourse.model;

import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private Epic epic;
    private Subtask subtask1, subtask2;

    @BeforeEach
    void setUp() {
        epic = new Epic("Test epic", "Test description");
        subtask1 = new Subtask("Subtask 1", "Desc", 1);
        subtask1.setId(2);
        subtask2 = new Subtask("Subtask 2", "Desc", 1);
        subtask2.setId(3);
    }

    @Test
    @DisplayName("Should update epic status correctly")
    void shouldUpdateEpicStatusCorrectly() {
        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.NEW);

        List<Subtask> subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);

        epic.addSubtaskId(subtask1.getId());
        epic.addSubtaskId(subtask2.getId());

        epic.updateStatus(subtasks);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    @DisplayName("Epic status should be DONE when all subtasks are done")
    void shouldUpdateStatusToDone() {
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);

        List<Subtask> subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);

        epic.addSubtaskId(subtask1.getId());
        epic.addSubtaskId(subtask2.getId());

        epic.updateStatus(subtasks);
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    @DisplayName("Epic status should be IN_PROGRESS when subtasks mixed")
    void shouldUpdateStatusToInProgress() {
        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.DONE);

        List<Subtask> subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);

        epic.addSubtaskId(subtask1.getId());
        epic.addSubtaskId(subtask2.getId());

        epic.updateStatus(subtasks);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    @DisplayName("Epic status should be NEW when no subtasks")
    void shouldHandleEmptySubtasks() {
        List<Subtask> subtasks = new ArrayList<>();
        epic.updateStatus(subtasks);
        assertEquals(Status.NEW, epic.getStatus());
    }
}