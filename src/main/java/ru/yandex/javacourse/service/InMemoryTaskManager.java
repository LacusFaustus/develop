package ru.yandex.javacourse.service;

import ru.yandex.javacourse.model.*;
import ru.yandex.javacourse.util.Managers;
import java.util.*;
import java.util.stream.Collectors;
import static java.util.Objects.requireNonNull;
import java.util.Collections;
import java.util.Objects;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int idCounter = 0;

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
            return new Task(task.getId(), task.getName(), task.getDescription(), task.getStatus());
        }
        return null;
    }

    @Override
    public synchronized int createTask(Task task) {
        requireNonNull(task, "Task cannot be null");
        int newId = ++idCounter;
        Task newTask = new Task(newId, task.getName(), task.getDescription(), task.getStatus());
        tasks.put(newId, newTask);
        return newId;
    }

    @Override
    public void updateTask(Task updatedTask) {
        requireNonNull(updatedTask, "Task cannot be null");
        if (tasks.containsKey(updatedTask.getId())) {
            tasks.put(updatedTask.getId(), updatedTask);
        }
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteAllTasks() {
        tasks.keySet().forEach(historyManager::remove);
        tasks.clear();
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
            Epic copy = new Epic(epic.getName(), epic.getDescription());
            copy.setId(epic.getId());
            copy.setStatus(epic.getStatus());
            copy.getSubtaskIds().addAll(epic.getSubtaskIds());
            return copy;
        }
        return null;
    }

    @Override
    public synchronized int createEpic(Epic epic) {
        requireNonNull(epic, "Epic cannot be null");
        int newId = ++idCounter;
        Epic newEpic = new Epic(epic.getName(), epic.getDescription());
        newEpic.setId(newId);
        epics.put(newId, newEpic);
        return newId;
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        requireNonNull(updatedEpic, "Epic cannot be null");
        Epic existingEpic = epics.get(updatedEpic.getId());
        if (existingEpic != null) {
            existingEpic.setName(updatedEpic.getName());
            existingEpic.setDescription(updatedEpic.getDescription());
        }
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            epic.getSubtaskIds().forEach(subtaskId -> {
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            });
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteAllEpics() {
        epics.keySet().forEach(historyManager::remove);
        subtasks.keySet().forEach(historyManager::remove);
        epics.clear();
        subtasks.clear();
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
            return new Subtask(subtask.getId(), subtask.getName(),
                    subtask.getDescription(), subtask.getStatus(),
                    subtask.getEpicId());
        }
        return null;
    }

    @Override
    public synchronized int createSubtask(Subtask subtask) {
        requireNonNull(subtask, "Subtask cannot be null");
        if (!epics.containsKey(subtask.getEpicId())) {
            return -1;
        }

        int newId = ++idCounter;
        Subtask newSubtask = new Subtask(newId,
                subtask.getName(),
                subtask.getDescription(),
                subtask.getStatus(),
                subtask.getEpicId());
        subtasks.put(newId, newSubtask);

        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtaskId(newId);
        updateEpicStatus(epic.getId());

        return newId;
    }

    @Override
    public void updateSubtask(Subtask updatedSubtask) {
        requireNonNull(updatedSubtask, "Subtask cannot be null");
        if (subtasks.containsKey(updatedSubtask.getId())) {
            subtasks.put(updatedSubtask.getId(), updatedSubtask);
            updateEpicStatus(updatedSubtask.getEpicId());
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtaskId(id);
                updateEpicStatus(epic.getId());
            }
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.keySet().forEach(historyManager::remove);
        subtasks.clear();
        epics.values().forEach(epic -> {
            epic.clearSubtaskIds();
            updateEpicStatus(epic.getId());
        });
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return Collections.emptyList();
        }

        return epic.getSubtaskIds().stream()
                .map(subtasks::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            List<Subtask> epicSubtasks = getSubtasksByEpicId(epicId);
            epic.updateStatus(epicSubtasks);
        }
    }
}