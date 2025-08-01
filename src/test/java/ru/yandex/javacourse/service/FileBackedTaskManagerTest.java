package ru.yandex.javacourse.service;

import org.junit.jupiter.api.*;
import ru.yandex.javacourse.model.*;

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
        // Создаем и сразу удаляем задачу
        Task task = new Task("Test", "Description");
        int taskId = manager.createTask(task);
        manager.deleteTaskById(taskId);

        // Загружаем состояние из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Проверяем, что все коллекции пусты
        assertAll(
                () -> assertTrue(loadedManager.getAllTasks().isEmpty(), "Список задач должен быть пустым"),
                () -> assertTrue(loadedManager.getAllEpics().isEmpty(), "Список эпиков должен быть пустым"),
                () -> assertTrue(loadedManager.getAllSubtasks().isEmpty(), "Список подзадач должен быть пустым"),
                () -> assertTrue(loadedManager.getHistory().isEmpty(), "История должна быть пустой")
        );
    }

    @Test
    @DisplayName("Сохранение и загрузка задач всех типов")
    void saveAndLoadTasks() {
        // Создаем задачи всех типов
        Task task = new Task("Task", "Description");
        int taskId = manager.createTask(task);

        Epic epic = new Epic("Epic", "Description");
        int epicId = manager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask", "Description", epicId);
        int subtaskId = manager.createSubtask(subtask);

        // Загружаем состояние из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Проверяем загруженные задачи
        Task loadedTask = loadedManager.getTaskById(taskId);
        assertAll(
                () -> assertNotNull(loadedTask, "Задача должна быть загружена"),
                () -> assertEquals("Task", loadedTask.getName(), "Название задачи"),
                () -> assertEquals("Description", loadedTask.getDescription(), "Описание задачи"),
                () -> assertEquals(Status.NEW, loadedTask.getStatus(), "Статус задачи")
        );

        // Проверяем загруженный эпик
        Epic loadedEpic = loadedManager.getEpicById(epicId);
        assertAll(
                () -> assertNotNull(loadedEpic, "Эпик должен быть загружен"),
                () -> assertEquals("Epic", loadedEpic.getName(), "Название эпика"),
                () -> assertEquals("Description", loadedEpic.getDescription(), "Описание эпика"),
                () -> assertEquals(Status.NEW, loadedEpic.getStatus(), "Статус эпика"),
                () -> assertTrue(loadedEpic.getSubtaskIds().contains(subtaskId), "Эпик должен содержать подзадачу")
        );

        // Проверяем загруженную подзадачу
        Subtask loadedSubtask = loadedManager.getSubtaskById(subtaskId);
        assertAll(
                () -> assertNotNull(loadedSubtask, "Подзадача должна быть загружена"),
                () -> assertEquals("Subtask", loadedSubtask.getName(), "Название подзадачи"),
                () -> assertEquals("Description", loadedSubtask.getDescription(), "Описание подзадачи"),
                () -> assertEquals(Status.NEW, loadedSubtask.getStatus(), "Статус подзадачи"),
                () -> assertEquals(epicId, loadedSubtask.getEpicId(), "ID эпика подзадачи")
        );
    }

    @Test
    @DisplayName("Обновление статуса эпика при изменении подзадачи")
    void shouldUpdateEpicStatusWhenSubtaskChanges() {
        // Создаем эпик и подзадачу
        Epic epic = new Epic("Epic", "Description");
        int epicId = manager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask", "Description", epicId);
        int subtaskId = manager.createSubtask(subtask);

        // Обновляем статус подзадачи
        Subtask updatedSubtask = new Subtask(
                subtaskId,
                "Updated Subtask",
                "Updated Description",
                Status.DONE,
                epicId
        );
        manager.updateSubtask(updatedSubtask);

        // Загружаем состояние из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Проверяем статус эпика
        Epic loadedEpic = loadedManager.getEpicById(epicId);
        assertNotNull(loadedEpic, "Эпик должен существовать");
        assertEquals(Status.DONE, loadedEpic.getStatus(), "Статус эпика должен быть DONE");
    }

    @Test
    @DisplayName("Сохранение и восстановление истории просмотров")
    void shouldPreserveHistory() {
        // Создаем две задачи
        Task task1 = new Task("Task 1", "Description");
        int taskId1 = manager.createTask(task1);

        Task task2 = new Task("Task 2", "Description");
        int taskId2 = manager.createTask(task2);

        // Добавляем задачи в историю просмотров
        manager.getTaskById(taskId1);
        manager.getTaskById(taskId2);

        // Загружаем состояние из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Проверяем историю
        List<Task> history = loadedManager.getHistory();
        assertAll(
                () -> assertEquals(2, history.size(), "История должна содержать 2 элемента"),
                () -> assertEquals(taskId1, history.get(0).getId(), "Первым должен быть taskId1"),
                () -> assertEquals(taskId2, history.get(1).getId(), "Вторым должен быть taskId2")
        );
    }

    @Test
    @DisplayName("Загрузка менеджера с пустой историей")
    void shouldHandleEmptyHistory() {
        // Создаем задачу без просмотра
        manager.createTask(new Task("Task", "Description"));

        // Загружаем состояние из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Проверяем, что история пуста
        assertTrue(loadedManager.getHistory().isEmpty(), "История должна быть пустой");
    }

    @Test
    @DisplayName("Обработка некорректных данных в истории")
    void shouldHandleCorruptedHistory() throws IOException {
        // Создаем тестовый файл с некорректной историей
        Files.write(tempFile.toPath(), List.of(
                "id,type,name,status,description,epic",
                "1,TASK,Test Task,NEW,Description,",  // Задача
                "",                                   // Пустая строка перед историей
                "1,invalid,3"                        // Некорректные ID в истории
        ));

        // Загружаем состояние из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Проверяем результаты
        assertAll(
                () -> assertNotNull(loadedManager.getTaskById(1), "Задача должна быть загружена"),
                () -> assertEquals(1, loadedManager.getHistory().size(), "История должна содержать 1 элемент"),
                () -> assertEquals(1, loadedManager.getHistory().get(0).getId(), "История должна содержать ID=1")
        );
    }

    @Test
    @DisplayName("Сохранение и загрузка большого количества задач")
    void shouldHandleLargeNumberOfTasks() {
        // Создаем 100 задач
        for (int i = 1; i <= 100; i++) {
            Task task = new Task("Task " + i, "Description " + i);
            int taskId = manager.createTask(task);
            manager.getTaskById(taskId); // Добавляем в историю
        }

        // Загружаем состояние из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Проверяем результаты
        assertAll(
                () -> assertEquals(100, loadedManager.getAllTasks().size(), "Должно быть 100 задач"),
                () -> assertEquals(100, loadedManager.getHistory().size(), "История должна содержать 100 элементов")
        );
    }

    @Test
    @DisplayName("Обновление задачи после загрузки")
    void shouldUpdateTaskAfterLoading() {
        // Создаем задачу
        Task task = new Task("Original", "Description");
        int taskId = manager.createTask(task);

        // Загружаем состояние из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Обновляем задачу в загруженном менеджере
        Task updatedTask = new Task(taskId, "Updated", "Updated", Status.IN_PROGRESS);
        loadedManager.updateTask(updatedTask);

        // Проверяем обновление
        Task loadedTask = loadedManager.getTaskById(taskId);
        assertAll(
                () -> assertEquals("Updated", loadedTask.getName(), "Название должно обновиться"),
                () -> assertEquals("Updated", loadedTask.getDescription(), "Описание должно обновиться"),
                () -> assertEquals(Status.IN_PROGRESS, loadedTask.getStatus(), "Статус должен обновиться")
        );
    }

    @Test
    @DisplayName("Удаление эпика с подзадачами после загрузки")
    void shouldDeleteEpicWithSubtasksAfterLoading() {
        // Создаем эпик и подзадачи
        Epic epic = new Epic("Epic", "Description");
        int epicId = manager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description", epicId);
        int subtask1Id = manager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Description", epicId);
        int subtask2Id = manager.createSubtask(subtask2);

        // Загружаем состояние из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Удаляем эпик
        loadedManager.deleteEpicById(epicId);

        // Проверяем удаление
        assertAll(
                () -> assertNull(loadedManager.getEpicById(epicId), "Эпик должен быть удален"),
                () -> assertNull(loadedManager.getSubtaskById(subtask1Id), "Подзадача 1 должна быть удалена"),
                () -> assertNull(loadedManager.getSubtaskById(subtask2Id), "Подзадача 2 должна быть удалена")
        );
    }

    @Test
    @DisplayName("Восстановление связей эпиков и подзадач")
    void shouldRestoreEpicSubtaskRelations() {
        // Создаем эпик и подзадачи
        Epic epic = new Epic("Epic", "Description");
        int epicId = manager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description", epicId);
        int subtask1Id = manager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Description", epicId);
        int subtask2Id = manager.createSubtask(subtask2);

        // Загружаем состояние из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Проверяем восстановление связей
        Epic loadedEpic = loadedManager.getEpicById(epicId);
        assertAll(
                () -> assertNotNull(loadedEpic, "Эпик должен быть загружен"),
                () -> assertEquals(2, loadedEpic.getSubtaskIds().size(), "Эпик должен содержать 2 подзадачи"),
                () -> assertTrue(loadedEpic.getSubtaskIds().contains(subtask1Id), "Эпик должен содержать подзадачу 1"),
                () -> assertTrue(loadedEpic.getSubtaskIds().contains(subtask2Id), "Эпик должен содержать подзадачу 2")
        );
    }

    @Test
    @DisplayName("Проверка изоляции загруженных объектов")
    void loadedObjectsShouldBeIsolated() {
        // Создаем задачу
        Task task = new Task("Original", "Description");
        int taskId = manager.createTask(task);

        // Загружаем состояние из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Клонируем объект перед изменением
        Task loadedTask = new Task(loadedManager.getTaskById(taskId));
        loadedTask.setName("Modified");
        loadedTask.setStatus(Status.DONE);

        // Проверяем, что состояние в менеджере не изменилось
        Task shouldBeOriginal = loadedManager.getTaskById(taskId);
        assertAll(
                () -> assertEquals("Original", shouldBeOriginal.getName(), "Название не должно измениться"),
                () -> assertEquals(Status.NEW, shouldBeOriginal.getStatus(), "Статус не должен измениться")
        );
    }
}