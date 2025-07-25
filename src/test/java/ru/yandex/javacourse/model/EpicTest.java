package test.java.ru.yandex.javacourse.model;

import main.java.ru.yandex.javacourse.model.*;
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
        subtask2 = new Subtask("Subtask 2", "Desc", 1);
    }

    @Test
    @DisplayName("Должен правильно обновлять статус эпика")
    void shouldUpdateEpicStatusCorrectly() {
        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.NEW);
        List<Subtask> subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);
        epic.updateStatus(subtasks);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    @DisplayName("Статус эпика без подзадач")
    void shouldHaveNewStatusWithNoSubtasks() {
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    @DisplayName("Статус эпика с новыми подзадачами")
    void shouldHaveNewStatusWhenAllSubtasksNew() {
        List<Subtask> subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);
        epic.updateStatus(subtasks);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    @DisplayName("Статус эпика с выполненными подзадачами")
    void shouldHaveDoneStatusWhenAllSubtasksDone() {
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        List<Subtask> subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);
        epic.updateStatus(subtasks);
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    @DisplayName("Статус эпика с разными статусами подзадач")
    void shouldHaveInProgressStatusForMixedSubtasks() {
        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.DONE);
        List<Subtask> subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);
        epic.updateStatus(subtasks);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }
}