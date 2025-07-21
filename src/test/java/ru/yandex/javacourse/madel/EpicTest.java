package ru.yandex.javacourse.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private static final String EPIC_NAME = "Test Epic";
    private static final String EPIC_DESCRIPTION = "Test Description";

    @Test
    @DisplayName("Epic should have NEW status when no subtasks")
    void getStatus_shouldReturnNew_whenNoSubtasks() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESCRIPTION);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    @DisplayName("Epic should correctly add and remove subtask IDs")
    void subtaskIdsManagement_shouldWorkCorrectly() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESCRIPTION);
        epic.addSubtaskId(1);
        epic.addSubtaskId(2);

        assertEquals(2, epic.getSubtaskIds().size());
        assertTrue(epic.getSubtaskIds().contains(1));

        epic.removeSubtaskId(1);
        assertEquals(1, epic.getSubtaskIds().size());

        epic.clearSubtaskIds();
        assertTrue(epic.getSubtaskIds().isEmpty());
    }
}