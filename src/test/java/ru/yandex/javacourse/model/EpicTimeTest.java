package ru.yandex.javacourse.model;

import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.service.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTimeTest {
    @Test
    void calculateEpicTime() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic = new Epic("Epic", "Description");
        int epicId = manager.createEpic(epic);

        Subtask subtask1 = new Subtask("Sub1", "Desc", epicId);
        subtask1.setStartTime(LocalDateTime.now());
        subtask1.setDuration(Duration.ofHours(2));
        manager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Sub2", "Desc", epicId);
        subtask2.setStartTime(LocalDateTime.now().plusHours(3));
        subtask2.setDuration(Duration.ofHours(1));
        manager.createSubtask(subtask2);

        Epic savedEpic = manager.getEpicById(epicId);
        assertEquals(subtask1.getStartTime(), savedEpic.getStartTime());
        assertEquals(Duration.ofHours(3), savedEpic.getDuration());
        assertEquals(subtask2.getEndTime(), savedEpic.getEndTime());
    }
}