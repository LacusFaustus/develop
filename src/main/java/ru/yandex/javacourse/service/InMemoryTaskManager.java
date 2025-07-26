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

    @Override
    public List<Task> getAllTasks() {
        return tasks.values().stream()
                .map(this::copyTask)
                .collect(Collectors.toList());
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
            return copyTask(task);
        }
        return null;
    }

    @Override
    public int createTask(Task task) {
        Objects.requireNonNull(task, "Task cannot be null");
        task.setId(++idCounter);
        tasks.put(task.getId(), copyTask(task));
        return task.getId();
    }

    @Override
    public void updateTask(Task updatedTask) {
        if (updatedTask != null && tasks.containsKey(updatedTask.getId())) {
            Task task = tasks.get(updatedTask.getId());
            task.setName(updatedTask.getName());
            task.setDescription(updatedTask.getDescription());
            task.setStatus(updatedTask.getStatus());
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
        return epics.values().stream()
                .map(this::copyEpic)
                .collect(Collectors.toList());
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
            return copyEpic(epic);
        }
        return null;
    }

    @Override
    public int createEpic(Epic epic) {
        Objects.requireNonNull(epic, "Epic cannot be null");
        epic.setId(++idCounter);
        epics.put(epic.getId(), copyEpic(epic));
        return epic.getId();
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        if (updatedEpic != null && epics.containsKey(updatedEpic.getId())) {
            Epic epic = epics.get(updatedEpic.getId());
            epic.setName(updatedEpic.getName());
            epic.setDescription(updatedEpic.getDescription());
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
        return subtasks.values().stream()
                .map(this::copySubtask)
                .collect(Collectors.toList());
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
            return copySubtask(subtask);
        }
        return null;
    }

    @Override
    public int createSubtask(Subtask subtask) {
        Objects.requireNonNull(subtask, "Subtask cannot be null");
        if (!epics.containsKey(subtask.getEpicId())) {
            throw new IllegalArgumentException("Epic not found for subtask");
        }

        subtask.setId(++idCounter);
        subtasks.put(subtask.getId(), copySubtask(subtask));

        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtaskId(subtask.getId());
        updateEpicStatus(epic.getId());

        return subtask.getId();
    }

    @Override
    public void updateSubtask(Subtask updatedSubtask) {
        if (updatedSubtask != null && subtasks.containsKey(updatedSubtask.getId())) {
            Subtask subtask = subtasks.get(updatedSubtask.getId());
            subtask.setName(updatedSubtask.getName());
            subtask.setDescription(updatedSubtask.getDescription());
            subtask.setStatus(updatedSubtask.getStatus());
            updateEpicStatus(subtask.getEpicId());
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
        if (epic == null) return;

        List<Subtask> epicSubtasks = getSubtasksByEpicId(epicId);
        epic.updateStatus(epicSubtasks);
    }

    private Task copyTask(Task original) {
        if (original == null) {
            return null;
        }
        return new Task(original.getId(), original.getName(), original.getDescription(), original.getStatus());
    }

    private Epic copyEpic(Epic original) {
        if (original == null) {
            return null;
        }
        Epic copy = new Epic(original.getId(), original.getName(), original.getDescription(), original.getStatus());
        original.getSubtaskIds().forEach(copy::addSubtaskId);
        return copy;
    }

    private Subtask copySubtask(Subtask original) {
        if (original == null) {
            return null;
        }
        return new Subtask(original.getId(), original.getName(), original.getDescription(),
                original.getStatus(), original.getEpicId());
    }
}