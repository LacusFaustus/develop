package ru.yandex.javacourse.service;

import ru.yandex.javacourse.model.*;
import ru.yandex.javacourse.util.Managers;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int idCounter = 0;

    // Защитное копирование задач
    private Task copyTask(Task original) {
        if (original == null) return null;
        Task copy = new Task(original.getName(), original.getDescription());
        copy.setId(original.getId());
        copy.setStatus(original.getStatus());
        return copy;
    }

    private Epic copyEpic(Epic original) {
        if (original == null) return null;
        Epic copy = new Epic(original.getName(), original.getDescription());
        copy.setId(original.getId());
        copy.setStatus(original.getStatus());
        original.getSubtaskIds().forEach(copy::addSubtaskId);
        return copy;
    }

    private Subtask copySubtask(Subtask original) {
        if (original == null) return null;
        Subtask copy = new Subtask(original.getName(), original.getDescription(), original.getEpicId());
        copy.setId(original.getId());
        copy.setStatus(original.getStatus());
        return copy;
    }

    @Override
    public List<Task> getAllTasks() {
        return tasks.values().stream()
                .map(this::copyTask)
                .collect(Collectors.toList());
    }

    @Override
    public Task getTaskById(int id) {
        Task task = copyTask(tasks.get(id));
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public int createTask(Task task) {
        Objects.requireNonNull(task, "Task cannot be null");
        Task taskCopy = copyTask(task);
        taskCopy.setId(++idCounter);
        tasks.put(taskCopy.getId(), taskCopy);
        return taskCopy.getId();
    }

    @Override
    public void updateTask(Task updatedTask) {
        if (updatedTask == null || !tasks.containsKey(updatedTask.getId())) {
            return;
        }
        Task taskCopy = copyTask(updatedTask);
        tasks.put(taskCopy.getId(), taskCopy);
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
        return epics.values().stream()
                .map(this::copyEpic)
                .collect(Collectors.toList());
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = copyEpic(epics.get(id));
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public int createEpic(Epic epic) {
        Objects.requireNonNull(epic, "Epic cannot be null");
        Epic epicCopy = copyEpic(epic);
        epicCopy.setId(++idCounter);
        epics.put(epicCopy.getId(), epicCopy);
        return epicCopy.getId();
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        if (updatedEpic == null || !epics.containsKey(updatedEpic.getId())) {
            return;
        }

        Epic existingEpic = epics.get(updatedEpic.getId());
        existingEpic.setName(updatedEpic.getName());
        existingEpic.setDescription(updatedEpic.getDescription());
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
        return subtasks.values().stream()
                .map(this::copySubtask)
                .collect(Collectors.toList());
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = copySubtask(subtasks.get(id));
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public int createSubtask(Subtask subtask) {
        Objects.requireNonNull(subtask, "Subtask cannot be null");
        if (!epics.containsKey(subtask.getEpicId())) {
            throw new IllegalArgumentException("Epic not found for subtask");
        }

        Subtask subtaskCopy = copySubtask(subtask);
        subtaskCopy.setId(++idCounter);
        subtasks.put(subtaskCopy.getId(), subtaskCopy);

        Epic epic = epics.get(subtaskCopy.getEpicId());
        epic.addSubtaskId(subtaskCopy.getId());
        updateEpicStatus(epic.getId());

        return subtaskCopy.getId();
    }

    @Override
    public void updateSubtask(Subtask updatedSubtask) {
        if (updatedSubtask == null || !subtasks.containsKey(updatedSubtask.getId())) {
            return;
        }

        Subtask subtaskCopy = copySubtask(updatedSubtask);
        subtasks.put(subtaskCopy.getId(), subtaskCopy);
        updateEpicStatus(subtaskCopy.getEpicId());
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
                .map(this::copySubtask)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory().stream()
                .map(task -> {
                    if (task instanceof Epic) {
                        return copyEpic((Epic) task);
                    } else if (task instanceof Subtask) {
                        return copySubtask((Subtask) task);
                    } else {
                        return copyTask(task);
                    }
                })
                .collect(Collectors.toList());
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            List<Subtask> epicSubtasks = getSubtasksByEpicId(epicId);
            epic.updateStatus(epicSubtasks);
        }
    }
}