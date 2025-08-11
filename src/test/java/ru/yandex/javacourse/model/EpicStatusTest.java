package ru.yandex.javacourse.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.service.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicStatusTest {
    private InMemoryTaskManager manager;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
    }

    @Test
    void epicStatusShouldBeNewWhenAllSubtasksNew() {
        Epic epic = new Epic("Epic", "Description");
        int epicId = manager.createEpic(epic);

        Subtask subtask1 = new Subtask("Sub 1", "Desc", epicId, Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask("Sub 2", "Desc", epicId, Status.NEW,
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(30));

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        assertEquals(Status.NEW, manager.getEpicById(epicId).getStatus());
    }

    @Test
    void epicStatusShouldBeDoneWhenAllSubtasksDone() {
        Epic epic = new Epic("Epic", "Description");
        int epicId = manager.createEpic(epic);

        Subtask subtask1 = new Subtask("Sub 1", "Desc", epicId, Status.DONE,
                LocalDateTime.now(), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask("Sub 2", "Desc", epicId, Status.DONE,
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(30));

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        assertEquals(Status.DONE, manager.getEpicById(epicId).getStatus());
    }

    @Test
    void epicStatusShouldBeInProgressWhenSubtasksNewAndDone() {
        Epic epic = new Epic("Epic", "Description");
        int epicId = manager.createEpic(epic);

        Subtask subtask1 = new Subtask("Sub 1", "Desc", epicId, Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask("Sub 2", "Desc", epicId, Status.DONE,
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(30));

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, manager.getEpicById(epicId).getStatus());
    }

    @Test
    void epicStatusShouldBeInProgressWhenSubtasksInProgress() {
        Epic epic = new Epic("Epic", "Description");
        int epicId = manager.createEpic(epic);

        Subtask subtask1 = new Subtask("Sub 1", "Desc", epicId, Status.IN_PROGRESS,
                LocalDateTime.now(), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask("Sub 2", "Desc", epicId, Status.IN_PROGRESS,
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(30));

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, manager.getEpicById(epicId).getStatus());
    }
}