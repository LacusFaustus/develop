package ru.yandex.javacourse.service;

import org.junit.jupiter.api.*;
import ru.yandex.javacourse.model.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    private File tempFile;
    private FileBackedTaskManager manager;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = Files.createTempFile("tasks", ".csv").toFile();
        manager = new FileBackedTaskManager(tempFile);
    }

    @AfterEach
    void tearDown() {
        tempFile.delete();
    }

    @Test
    void testSaveAndLoadEpicTimeFields() {
        Epic epic = new Epic("Epic", "Description");
        int epicId = manager.createEpic(epic);

        LocalDateTime startTime = LocalDateTime.of(2023, 1, 1, 10, 0);
        Duration duration = Duration.ofHours(1);

        Subtask subtask = new Subtask("Subtask", "Description", epicId,
                Status.NEW, startTime, duration);
        manager.createSubtask(subtask);

        // Явно обновляем время эпика перед сохранением
        ((InMemoryTaskManager)manager).updateEpicTime(epicId);
        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Обновляем время эпика после загрузки
        loadedManager.updateEpicTime(epicId);

        Epic loadedEpic = loadedManager.getEpicById(epicId);

        assertNotNull(loadedEpic);
        assertEquals(startTime, loadedEpic.getStartTime());
        assertEquals(duration, loadedEpic.getDuration());
    }
}