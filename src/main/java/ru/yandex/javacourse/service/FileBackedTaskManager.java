package ru.yandex.javacourse.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import ru.yandex.javacourse.exception.ManagerSaveException;
import ru.yandex.javacourse.model.Epic;
import ru.yandex.javacourse.model.Status;
import ru.yandex.javacourse.model.Subtask;
import ru.yandex.javacourse.model.Task;
import ru.yandex.javacourse.util.Managers;

/**
 * Менеджер задач, сохраняющий состояние в файл.
 */
public class FileBackedTaskManager extends InMemoryTaskManager {
    /** Файл для сохранения данных. */
    private final File file;
    /** Заголовок CSV файла. */
    private static final String CSV_HEADER = "id,type,name,status,description,epic,startTime,duration";
    /** Количество полей в CSV для задачи. */
    private static final int TASK_FIELDS_COUNT = 6;
    /** Индекс поля startTime в CSV. */
    private static final int START_TIME_INDEX = 6;
    /** Индекс поля duration в CSV. */
    private static final int DURATION_INDEX = 7;

    /**
     * Конструктор менеджера.
     *
     * @param file файл для сохранения данных
     */
    public FileBackedTaskManager(File file) {
        super(Managers.getDefaultHistory());
        this.file = file;
    }

    /**
     * Загружает менеджер из файла.
     *
     * @param file файл с данными
     * @return загруженный менеджер задач
     */
    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.load();
        return manager;
    }

    /** Загружает данные из файла. */
    private void load() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            reader.readLine(); // Пропускаем заголовок

            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                Task task = parseTask(line);
                if (task != null) {
                    addTaskToCollections(task);
                }
            }

            String historyLine = reader.readLine();
            if (historyLine != null && !historyLine.isEmpty()) {
                restoreHistory(parseHistory(historyLine));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки из файла", e);
        }
    }

    /**
     * Парсит задачу из строки CSV.
     *
     * @param line строка CSV
     * @return задача или null, если строка некорректна
     */
    private Task parseTask(final String line) {
        String[] fields = line.split(",", -1);
        if (fields.length < TASK_FIELDS_COUNT) {
            return null;
        }

        try {
            int id = Integer.parseInt(fields[0]);
            String type = fields[1];
            String name = fields[2];
            Status status = Status.valueOf(fields[3]);
            String description = fields[4];
            String epicId = fields[5];
            LocalDateTime startTime = fields.length > START_TIME_INDEX && !fields[START_TIME_INDEX].isEmpty()
                    ? LocalDateTime.parse(fields[START_TIME_INDEX]) : null;
            Duration duration = fields.length > DURATION_INDEX && !fields[DURATION_INDEX].isEmpty()
                    ? Duration.ofMinutes(Long.parseLong(fields[DURATION_INDEX])) : Duration.ZERO;

            switch (type) {
                case "TASK":
                    return new Task(id, name, description, status, startTime, duration);
                case "EPIC":
                    return new Epic(id, name, description, status);
                case "SUBTASK":
                    if (epicId.isEmpty()) {
                        return null;
                    }
                    Subtask subtask = new Subtask(id, name, description, Integer.parseInt(epicId), status);
                    subtask.setStartTime(startTime);
                    subtask.setDuration(duration);
                    return subtask;
                default:
                    return null;
            }
        } catch (Exception e) {
            throw new ManagerSaveException("Error parsing task from string: " + line, e);
        }
    }

    /**
     * Добавляет задачу в соответствующие коллекции.
     *
     * @param task задача для добавления
     */
    private void addTaskToCollections(final Task task) {
        if (task instanceof Epic) {
            epics.put(task.getId(), (Epic) task);
        } else if (task instanceof Subtask) {
            subtasks.put(task.getId(), (Subtask) task);
            Epic epic = epics.get(((Subtask) task).getEpicId());
            if (epic != null) {
                epic.addSubtaskId(task.getId());
            }
        } else {
            tasks.put(task.getId(), task);
        }

        if (task.getId() >= nextId) {
            nextId = task.getId() + 1;
        }
    }

    /**
     * Восстанавливает историю просмотров.
     *
     * @param historyIds список ID задач в порядке просмотра
     */
    private void restoreHistory(final List<Integer> historyIds) {
        if (historyIds == null) {
            return;
        }

        for (int id : historyIds) {
            Task task = findTaskById(id);
            if (task != null) {
                historyManager.add(task);
            }
        }
    }

    /**
     * Находит задачу по ID.
     *
     * @param id ID задачи
     * @return найденная задача или null
     */
    private Task findTaskById(final int id) {
        Task task = tasks.get(id);
        if (task != null) {
            return task;
        }

        task = epics.get(id);
        if (task != null) {
            return task;
        }

        return subtasks.get(id);
    }

    /**
     * Парсит историю из строки.
     *
     * @param value строка с историей
     * @return список ID задач
     */
    private static List<Integer> parseHistory(final String value) {
        if (value == null || value.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(value.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    /**
     * Преобразует задачу в строку CSV.
     *
     * @param task задача
     * @return строка CSV
     */
    private String taskToString(final Task task) {
        String type = task.getType();
        String[] fields = {
                String.valueOf(task.getId()),
                type,
                task.getName(),
                task.getStatus().name(),
                task.getDescription(),
                type.equals("SUBTASK") ? String.valueOf(((Subtask) task).getEpicId()) : "",
                task.getStartTime() != null ? task.getStartTime().toString() : "",
                task.getDuration() != null ? String.valueOf(task.getDuration().toMinutes()) : ""
        };
        return String.join(",", fields);
    }

    /**
     * Преобразует историю в строку.
     *
     * @param manager менеджер истории
     * @return строка с историей
     */
    private String historyToString(final HistoryManager manager) {
        return manager.getHistory().stream()
                .map(Task::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    /** Сохраняет текущее состояние в файл. */
    protected void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write(CSV_HEADER);
            writer.newLine();

            for (Task task : tasks.values()) {
                writer.write(taskToString(task));
                writer.newLine();
            }

            for (Epic epic : epics.values()) {
                writer.write(taskToString(epic));
                writer.newLine();
            }

            for (Subtask subtask : subtasks.values()) {
                writer.write(taskToString(subtask));
                writer.newLine();
            }

            writer.newLine();
            writer.write(historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Failed to save to file", e);
        }
    }

    @Override
    public int createTask(final Task task) {
        int id = super.createTask(task);
        save();
        return id;
    }

    @Override
    public int createEpic(final Epic epic) {
        int id = super.createEpic(epic);
        save();
        return id;
    }

    @Override
    public int createSubtask(final Subtask subtask) {
        int id = super.createSubtask(subtask);
        save();
        return id;
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return super.getAllSubtasks();
    }

    @Override
    public void updateTask(final Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(final Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(final Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(final int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(final int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(final int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }
}