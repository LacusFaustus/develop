package ru.yandex.javacourse.praktikum.service;

import org.junit.jupiter.api.*;
import ru.yandex.javacourse.praktikum.service.FileBackedTaskManager;
import ru.yandex.javacourse.praktikum.model.Epic;
import ru.yandex.javacourse.praktikum.model.Status;
import ru.yandex.javacourse.praktikum.model.Subtask;
import ru.yandex.javacourse.praktikum.model.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    private File tempFile;
    private FileBackedTaskManager manager;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("tasks", ".csv");
        manager = new FileBackedTaskManager(tempFile);
    }

    @AfterEach
    void tearDown() {
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    @DisplayName("Сохранение и загрузка пустого менеджера")
    void saveAndLoadEmptyManager() {
        Task task = new Task("Test", "Description");
        int taskId = manager.createTask(task);
        manager.deleteTaskById(taskId);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(loadedManager.getAllTasks().isEmpty());
        assertTrue(loadedManager.getAllEpics().isEmpty());
        assertTrue(loadedManager.getAllSubtasks().isEmpty());
        assertTrue(loadedManager.getHistory().isEmpty());
    }

    @Test
    @DisplayName("Сохранение и загрузка задач всех типов")
    void saveAndLoadTasks() {
        Task task = new Task("Task", "Description");
        int taskId = manager.createTask(task);

        Epic epic = new Epic("Epic", "Description");
        int epicId = manager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask", "Description", epicId);
        int subtaskId = manager.createSubtask(subtask);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        Task loadedTask = loadedManager.getTaskById(taskId);
        assertNotNull(loadedTask);
        assertEquals("Task", loadedTask.getName());
        assertEquals("Description", loadedTask.getDescription());
        assertEquals(Status.NEW, loadedTask.getStatus());

        Epic loadedEpic = loadedManager.getEpicById(epicId);
        assertNotNull(loadedEpic);
        assertEquals("Epic", loadedEpic.getName());
        assertEquals("Description", loadedEpic.getDescription());
        assertEquals(Status.NEW, loadedEpic.getStatus());
        assertTrue(loadedEpic.getSubtaskIds().contains(subtaskId));

        Subtask loadedSubtask = loadedManager.getSubtaskById(subtaskId);
        assertNotNull(loadedSubtask);
        assertEquals("Subtask", loadedSubtask.getName());
        assertEquals("Description", loadedSubtask.getDescription());
        assertEquals(Status.NEW, loadedSubtask.getStatus());
        assertEquals(epicId, loadedSubtask.getEpicId());
    }

    @Test
    @DisplayName("Обновление статуса эпика при изменении подзадачи")
    void shouldUpdateEpicStatusWhenSubtaskChanges() {
        Epic epic = new Epic("Epic", "Description");
        int epicId = manager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask", "Description", epicId);
        int subtaskId = manager.createSubtask(subtask);

        Subtask updatedSubtask = new Subtask(
                subtaskId,
                "Updated Subtask",
                "Updated Description",
                Status.DONE,
                epicId
        );
        manager.updateSubtask(updatedSubtask);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        Epic loadedEpic = loadedManager.getEpicById(epicId);
        assertNotNull(loadedEpic);
        assertEquals(Status.DONE, loadedEpic.getStatus());
    }

    @Test
    @DisplayName("Сохранение и восстановление истории просмотров")
    void shouldPreserveHistory() {
        Task task1 = new Task("Task 1", "Description");
        int taskId1 = manager.createTask(task1);

        Task task2 = new Task("Task 2", "Description");
        int taskId2 = manager.createTask(task2);

        manager.getTaskById(taskId1);
        manager.getTaskById(taskId2);

        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        List<Task> history = loadedManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(taskId1, history.get(0).getId());
        assertEquals(taskId2, history.get(1).getId());
    }

    @Test
    @DisplayName("Загрузка менеджера с пустой историей")
    void shouldHandleEmptyHistory() {
        manager.createTask(new Task("Task", "Description"));

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(loadedManager.getHistory().isEmpty());
    }

    @Test
    @DisplayName("Обработка некорректных данных в истории")
    void shouldHandleCorruptedHistory() throws IOException {
        Files.write(tempFile.toPath(), List.of(
                "id,type,name,status,description,epic",
                "1,TASK,Test Task,NEW,Description,",
                "",
                "1,invalid,3"
        ));

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertNotNull(loadedManager.getTaskById(1));
        assertEquals(1, loadedManager.getHistory().size());
        assertEquals(1, loadedManager.getHistory().get(0).getId());
    }

    @Test
    @DisplayName("Сохранение и загрузка большого количества задач")
    void shouldHandleLargeNumberOfTasks() {
        for (int i = 1; i <= 100; i++) {
            Task task = new Task("Task " + i, "Description " + i);
            int taskId = manager.createTask(task);
            manager.getTaskById(taskId);
        }

        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(100, loadedManager.getAllTasks().size());
        assertEquals(100, loadedManager.getHistory().size());
    }

    @Test
    @DisplayName("Обновление задачи после загрузки")
    void shouldUpdateTaskAfterLoading() {
        Task task = new Task("Original", "Description");
        int taskId = manager.createTask(task);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        Task updatedTask = new Task(taskId, "Updated", "Updated", Status.IN_PROGRESS);
        loadedManager.updateTask(updatedTask);

        Task loadedTask = loadedManager.getTaskById(taskId);
        assertEquals("Updated", loadedTask.getName());
        assertEquals("Updated", loadedTask.getDescription());
        assertEquals(Status.IN_PROGRESS, loadedTask.getStatus());
    }

    @Test
    @DisplayName("Удаление эпика с подзадачами после загрузки")
    void shouldDeleteEpicWithSubtasksAfterLoading() {
        Epic epic = new Epic("Epic", "Description");
        int epicId = manager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description", epicId);
        int subtask1Id = manager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Description", epicId);
        int subtask2Id = manager.createSubtask(subtask2);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        loadedManager.deleteEpicById(epicId);

        assertNull(loadedManager.getEpicById(epicId));
        assertNull(loadedManager.getSubtaskById(subtask1Id));
        assertNull(loadedManager.getSubtaskById(subtask2Id));
    }

    @Test
    @DisplayName("Восстановление связей эпиков и подзадач")
    void shouldRestoreEpicSubtaskRelations() {
        Epic epic = new Epic("Epic", "Description");
        int epicId = manager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description", epicId);
        int subtask1Id = manager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Description", epicId);
        int subtask2Id = manager.createSubtask(subtask2);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        Epic loadedEpic = loadedManager.getEpicById(epicId);
        assertNotNull(loadedEpic);
        assertEquals(2, loadedEpic.getSubtaskIds().size());
        assertTrue(loadedEpic.getSubtaskIds().contains(subtask1Id));
        assertTrue(loadedEpic.getSubtaskIds().contains(subtask2Id));
    }

    @Test
    @DisplayName("Проверка изоляции загруженных объектов")
    void loadedObjectsShouldBeIsolated() {
        Task task = new Task("Original", "Description");
        int taskId = manager.createTask(task);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        Task loadedTask = new Task(loadedManager.getTaskById(taskId));
        loadedTask.setName("Modified");
        loadedTask.setStatus(Status.DONE);

        Task shouldBeOriginal = loadedManager.getTaskById(taskId);
        assertEquals("Original", shouldBeOriginal.getName());
        assertEquals(Status.NEW, shouldBeOriginal.getStatus());
    }
}