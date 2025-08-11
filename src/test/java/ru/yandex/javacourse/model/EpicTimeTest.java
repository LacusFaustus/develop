package ru.yandex.javacourse.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.service.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTimeTest {
    private InMemoryTaskManager manager;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
    }

    @Test
    void calculateEpicTime() {
        Epic epic = new Epic("Epic", "Description");
        int epicId = manager.createEpic(epic);

        Subtask subtask1 = new Subtask("Sub1", "Desc", epicId, Status.NEW,
                LocalDateTime.now(), Duration.ofHours(2));
        manager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Sub2", "Desc", epicId, Status.NEW,
                LocalDateTime.now().plusHours(3), Duration.ofHours(1));
        manager.createSubtask(subtask2);

        Epic savedEpic = manager.getEpicById(epicId);
        assertEquals(subtask1.getStartTime(), savedEpic.getStartTime());
        assertEquals(Duration.ofHours(3), savedEpic.getDuration());
        assertEquals(subtask2.getEndTime(), savedEpic.getEndTime());
    }
}