package ru.yandex.javacourse.service;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.javacourse.model.Epic;
import ru.yandex.javacourse.model.Status;
import ru.yandex.javacourse.model.Subtask;
import ru.yandex.javacourse.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public abstract class AbstractTaskManagerTest<T extends TaskManager> {
    protected T manager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;
    protected int taskId;
    protected int epicId;
    protected int subtaskId;

    @BeforeEach
    public void setUp() throws InterruptedException {
        initManager();

        LocalDateTime testStartTime = LocalDateTime.of(2023, 1, 1, 10, 0);

        task = new Task("Test task", "Test description", Status.NEW,
                testStartTime, Duration.ofHours(1));
        taskId = manager.createTask(task);

        epic = new Epic("Test epic", "Test description");
        epicId = manager.createEpic(epic);

        subtask = new Subtask("Test subtask", "Test description", epicId, Status.NEW,
                testStartTime.plusHours(2), Duration.ofHours(1));
        subtaskId = manager.createSubtask(subtask);
    }

    protected abstract void initManager();
}