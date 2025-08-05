package ru.yandex.javacourse.service;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.javacourse.model.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractTaskManagerTest<T extends TaskManager> {
    protected T manager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;
    protected int taskId;
    protected int epicId;
    protected int subtaskId;

    @BeforeEach
    public void setUp() {
        // Инициализация менеджера в подклассах
        initManager();

        // Создаем тестовые задачи
        task = new Task("Test task", "Test description");
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofHours(1));
        taskId = manager.createTask(task);

        epic = new Epic("Test epic", "Test description");
        epicId = manager.createEpic(epic);

        subtask = new Subtask("Test subtask", "Test description", epicId);
        subtask.setStartTime(LocalDateTime.now().plusHours(2));
        subtask.setDuration(Duration.ofHours(1));
        subtaskId = manager.createSubtask(subtask);
    }

    protected abstract void initManager();
}