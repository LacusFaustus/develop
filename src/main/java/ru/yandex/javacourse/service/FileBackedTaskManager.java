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

public class FileBackedTaskManager extends InMemoryTaskManager
{
    private final File file;
    private static final String CSV_HEADER = "id,type,name,status,description,epic,startTime,duration";

    public FileBackedTaskManager(File file)
    {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file)
    {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.load();
        return manager;
    }

    private void load()
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8)))
        {
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null)
            {
                if (line.isEmpty())
                {
                    break;
                }

                Task task = parseTask(line);
                if (task != null)
                {
                    addTaskToCollections(task);
                }
            }

            String historyLine = reader.readLine();
            if (historyLine != null && !historyLine.isEmpty())
            {
                restoreHistory(parseHistory(historyLine));
            }
        } catch (IOException e)
        {
            throw new ManagerSaveException("Failed to load from file", e);
        }
    }

    private Task parseTask(String line)
    {
        String[] fields = line.split(",", -1);
        if (fields.length < 6)
        {
            return null;
        }

        try
        {
            int id = Integer.parseInt(fields[0]);
            String type = fields[1];
            String name = fields[2];
            Status status = Status.valueOf(fields[3]);
            String description = fields[4];
            String epicId = fields[5];
            LocalDateTime startTime = fields.length > 6 && !fields[6].isEmpty()
                    ? LocalDateTime.parse(fields[6]) : null;
            Duration duration = fields.length > 7 && !fields[7].isEmpty()
                    ? Duration.ofMinutes(Long.parseLong(fields[7])) : Duration.ZERO;

            switch (type)
            {
                case "TASK":
                    return new Task(id, name, description, status, startTime, duration);
                case "EPIC":
                    return new Epic(id, name, description, status);
                case "SUBTASK":
                    if (epicId.isEmpty())
                    {
                        return null;
                    }
                    Subtask subtask = new Subtask(id, name, description, Integer.parseInt(epicId), status);
                    subtask.setStartTime(startTime);
                    subtask.setDuration(duration);
                    return subtask;
                default:
                    return null;
            }
        } catch (Exception e)
        {
            throw new ManagerSaveException("Error parsing task from string: " + line, e);
        }
    }

    private void addTaskToCollections(Task task)
    {
        if (task instanceof Epic)
        {
            epics.put(task.getId(), (Epic) task);
        } else if (task instanceof Subtask)
        {
            subtasks.put(task.getId(), (Subtask) task);
            Epic epic = epics.get(((Subtask) task).getEpicId());
            if (epic != null)
            {
                epic.addSubtaskId(task.getId());
            }
        } else
        {
            tasks.put(task.getId(), task);
        }

        if (task.getId() >= nextId)
        {
            nextId = task.getId() + 1;
        }
    }

    private void restoreHistory(List<Integer> historyIds)
    {
        if (historyIds == null)
        {
            return;
        }

        for (int id : historyIds)
        {
            Task task = findTaskById(id);
            if (task != null)
            {
                historyManager.add(task);
            }
        }
    }

    private Task findTaskById(int id)
    {
        Task task = tasks.get(id);
        if (task != null)
        {
            return task;
        }

        task = epics.get(id);
        if (task != null)
        {
            return task;
        }

        return subtasks.get(id);
    }

    private static List<Integer> parseHistory(String value)
    {
        if (value == null || value.trim().isEmpty())
        {
            return Collections.emptyList();
        }
        return Arrays.stream(value.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private String taskToString(Task task)
    {
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

    private String historyToString(HistoryManager manager)
    {
        return manager.getHistory().stream()
                .map(Task::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    protected void save()
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8)))
        {
            writer.write(CSV_HEADER);
            writer.newLine();

            for (Task task : tasks.values())
            {
                writer.write(taskToString(task));
                writer.newLine();
            }

            for (Epic epic : epics.values())
            {
                writer.write(taskToString(epic));
                writer.newLine();
            }

            for (Subtask subtask : subtasks.values())
            {
                writer.write(taskToString(subtask));
                writer.newLine();
            }

            writer.newLine();
            writer.write(historyToString(historyManager));
        } catch (IOException e)
        {
            throw new ManagerSaveException("Failed to save to file", e);
        }
    }

    @Override
    public int createTask(Task task)
    {
        int id = super.createTask(task);
        save();
        return id;
    }

    @Override
    public int createEpic(Epic epic)
    {
        int id = super.createEpic(epic);
        save();
        return id;
    }

    @Override
    public int createSubtask(Subtask subtask)
    {
        int id = super.createSubtask(subtask);
        save();
        return id;
    }

    @Override
    public void updateTask(Task task)
    {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic)
    {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask)
    {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int id)
    {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id)
    {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id)
    {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteAllTasks()
    {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics()
    {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks()
    {
        super.deleteAllSubtasks();
        save();
    }
}