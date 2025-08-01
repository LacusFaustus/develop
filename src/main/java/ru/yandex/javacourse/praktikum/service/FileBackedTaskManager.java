// src/main/java/ru/yandex/praktikum/service/FileBackedTaskManager.java
package ru.yandex.praktikum.service;

import ru.yandex.praktikum.exception.ManagerSaveException;
import ru.yandex.praktikum.model.*;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public int createTask(Task task) {
        int id = super.createTask(task);
        save();
        return id;
    }

    @Override
    public int createEpic(Epic epic) {
        int id = super.createEpic(epic);
        save();
        return id;
    }

    @Override
    public int createSubtask(Subtask subtask) {
        int id = super.createSubtask(subtask);
        save();
        return id;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic\n");

            List<Task> allTasks = new ArrayList<>();
            allTasks.addAll(getAllTasks());
            allTasks.addAll(getAllEpics());
            allTasks.addAll(getAllSubtasks());

            allTasks.sort(Comparator.comparingInt(Task::getId));

            for (Task task : allTasks) {
                writer.write(taskToString(task));
                writer.newLine();
            }

            writer.newLine();
            String history = historyToString();
            if (!history.isEmpty()) {
                writer.write(history);
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл", e);
        }
    }

    private String taskToString(Task task) {
        String[] fields = new String[6];
        fields[0] = String.valueOf(task.getId());
        fields[1] = task.getType().name();
        fields[2] = task.getName();
        fields[3] = task.getStatus().name();
        fields[4] = task.getDescription();
        fields[5] = (task instanceof Subtask) ? String.valueOf(((Subtask) task).getEpicId()) : "";
        return String.join(",", fields);
    }

    private String historyToString() {
        List<String> ids = new ArrayList<>();
        for (Task task : getHistory()) {
            ids.add(String.valueOf(task.getId()));
        }
        return String.join(",", ids);
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            if (lines.size() < 2) return manager;

            int maxId = 0;
            Map<Integer, Task> tasks = new HashMap<>();
            Map<Integer, Epic> epics = new HashMap<>();
            Map<Integer, Subtask> subtasks = new HashMap<>();
            List<Integer> historyIds = new ArrayList<>();

            boolean readingHistory = false;

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) {
                    readingHistory = true;
                    continue;
                }

                if (readingHistory) {
                    for (String id : line.split(",")) {
                        try {
                            historyIds.add(Integer.parseInt(id.trim()));
                        } catch (NumberFormatException e) {
                            System.err.println("Некорректный ID в истории: " + id);
                        }
                    }
                } else {
                    Task task = taskFromString(line);
                    if (task != null) {
                        int id = task.getId();
                        if (id > maxId) maxId = id;

                        if (task instanceof Epic) {
                            epics.put(id, (Epic) task);
                        } else if (task instanceof Subtask) {
                            subtasks.put(id, (Subtask) task);
                        } else {
                            tasks.put(id, task);
                        }
                    }
                }
            }

            restoreManagerState(manager, tasks, epics, subtasks, maxId);
            restoreHistory(manager, historyIds);

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки из файла", e);
        }
        return manager;
    }

    private static void restoreManagerState(FileBackedTaskManager manager,
                                            Map<Integer, Task> tasks,
                                            Map<Integer, Epic> epics,
                                            Map<Integer, Subtask> subtasks,
                                            int maxId) {
        manager.idCounter = maxId + 1;
        manager.tasks.putAll(tasks);
        manager.epics.putAll(epics);
        manager.subtasks.putAll(subtasks);

        for (Subtask subtask : subtasks.values()) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.addSubtaskId(subtask.getId());
            }
        }

        for (Epic epic : epics.values()) {
            List<Subtask> epicSubtasks = new ArrayList<>();
            for (int subtaskId : epic.getSubtaskIds()) {
                Subtask subtask = manager.subtasks.get(subtaskId);
                if (subtask != null) {
                    epicSubtasks.add(subtask);
                }
            }
            epic.updateStatus(epicSubtasks);
        }
    }

    private static void restoreHistory(FileBackedTaskManager manager, List<Integer> historyIds) {
        for (int id : historyIds) {
            Task task = manager.findTaskAnywhere(id);
            if (task != null) {
                manager.historyManager.add(task);
            }
        }
    }

    private Task findTaskAnywhere(int id) {
        Task task = tasks.get(id);
        if (task != null) return task;
        task = epics.get(id);
        if (task != null) return task;
        return subtasks.get(id);
    }

    private static Task taskFromString(String value) {
        try {
            String[] fields = value.split(",", -1);
            if (fields.length < 6) {
                System.err.println("Недостаточно полей в строке: " + value);
                return null;
            }

            int id = Integer.parseInt(fields[0]);
            TaskType type = TaskType.valueOf(fields[1]);
            String name = fields[2];
            Status status = Status.valueOf(fields[3]);
            String description = fields[4];
            String epicField = fields[5];

            switch (type) {
                case TASK:
                    return new Task(id, name, description, status);
                case EPIC:
                    return new Epic(id, name, description, status);
                case SUBTASK:
                    int epicId = epicField.isEmpty() ? 0 : Integer.parseInt(epicField);
                    return new Subtask(id, name, description, status, epicId);
                default:
                    System.err.println("Неизвестный тип задачи: " + type);
                    return null;
            }
        } catch (Exception e) {
            System.err.println("Ошибка разбора строки задачи: " + value);
            e.printStackTrace();
            return null;
        }
    }
}