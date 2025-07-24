package ru.yandex.javacourse.model;

import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private Epic epic;
    private Subtask subtask1, subtask2;

    @BeforeEach
    void setUp() {
        epic = new Epic("Epic", "Description");
        subtask1 = new Subtask("Subtask 1", "Description 1", epic.getId());
        subtask2 = new Subtask("Subtask 2", "Description 2", epic.getId());
    }

    @Test
    @DisplayName("Статус эпика без подзадач")
    void shouldHaveNewStatusWithNoSubtasks() {
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    @DisplayName("Статус эпика с новыми подзадачами")
    void shouldHaveNewStatusWhenAllSubtasksNew() {
        epic.updateStatus(List.of(subtask1, subtask2));
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    @DisplayName("Статус эпика с выполненными подзадачами")
    void shouldHaveDoneStatusWhenAllSubtasksDone() {
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        epic.updateStatus(List.of(subtask1, subtask2));
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    @DisplayName("Статус эпика с разными статусами подзадач")
    void shouldHaveInProgressStatusForMixedSubtasks() {
        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.DONE);
        epic.updateStatus(List.of(subtask1, subtask2));
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }
}