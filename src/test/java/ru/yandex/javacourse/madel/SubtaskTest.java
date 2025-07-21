package ru.yandex.javacourse.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    private static final String SUBTASK_NAME = "Test Subtask";
    private static final String SUBTASK_DESCRIPTION = "Test Description";
    private static final int EPIC_ID = 1;

    @Test
    @DisplayName("Subtask should correctly store epic ID")
    void getEpicId_shouldReturnCorrectEpicId() {
        Subtask subtask = new Subtask(SUBTASK_NAME, SUBTASK_DESCRIPTION, EPIC_ID);
        assertEquals(EPIC_ID, subtask.getEpicId());
    }

    @Test
    @DisplayName("Subtask should correctly update epic ID")
    void setEpicId_shouldUpdateEpicId() {
        Subtask subtask = new Subtask(SUBTASK_NAME, SUBTASK_DESCRIPTION, EPIC_ID);
        subtask.setEpicId(2);
        assertEquals(2, subtask.getEpicId());
    }
}