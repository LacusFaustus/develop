package ru.yandex.javacourse.service;

import ru.yandex.javacourse.model.*;
import ru.yandex.javacourse.util.Managers;
<<<<<<< HEAD

import java.util.*;
import java.util.stream.Collectors;
=======
import java.util.*;
import java.util.stream.Collectors;
import static java.util.Objects.requireNonNull;
import java.util.Collections;
import java.util.Objects;
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int idCounter = 0;

    @Override
    public List<Task> getAllTasks() {
<<<<<<< HEAD
        return tasks.values().stream()
                .map(this::copyTask)
                .collect(Collectors.toList());
=======
        return new ArrayList<>(tasks.values());
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
<<<<<<< HEAD
            return copyTask(task);
=======
            return new Task(task.getId(), task.getName(), task.getDescription(), task.getStatus());
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
        }
        return null;
    }

    @Override
<<<<<<< HEAD
    public int createTask(Task task) {
        Objects.requireNonNull(task, "Task cannot be null");
        task.setId(++idCounter);
        tasks.put(task.getId(), copyTask(task));
        return task.getId();
=======
    public synchronized int createTask(Task task) {
        requireNonNull(task, "Task cannot be null");
        int newId = ++idCounter;
        Task newTask = new Task(newId, task.getName(), task.getDescription(), task.getStatus());
        tasks.put(newId, newTask);
        return newId;
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
    }

    @Override
    public void updateTask(Task updatedTask) {
<<<<<<< HEAD
        if (updatedTask != null && tasks.containsKey(updatedTask.getId())) {
            Task task = tasks.get(updatedTask.getId());
            task.setName(updatedTask.getName());
            task.setDescription(updatedTask.getDescription());
            task.setStatus(updatedTask.getStatus());
=======
        requireNonNull(updatedTask, "Task cannot be null");
        if (tasks.containsKey(updatedTask.getId())) {
            tasks.put(updatedTask.getId(), updatedTask);
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
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
<<<<<<< HEAD
        return epics.values().stream()
                .map(this::copyEpic)
                .collect(Collectors.toList());
=======
        return new ArrayList<>(epics.values());
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
<<<<<<< HEAD
            return copyEpic(epic);
=======
            Epic copy = new Epic(epic.getName(), epic.getDescription());
            copy.setId(epic.getId());
            copy.setStatus(epic.getStatus());
            copy.getSubtaskIds().addAll(epic.getSubtaskIds());
            return copy;
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
        }
        return null;
    }

    @Override
<<<<<<< HEAD
    public int createEpic(Epic epic) {
        Objects.requireNonNull(epic, "Epic cannot be null");
        epic.setId(++idCounter);
        epics.put(epic.getId(), copyEpic(epic));
        return epic.getId();
=======
    public synchronized int createEpic(Epic epic) {
        requireNonNull(epic, "Epic cannot be null");
        int newId = ++idCounter;
        Epic newEpic = new Epic(epic.getName(), epic.getDescription());
        newEpic.setId(newId);
        epics.put(newId, newEpic);
        return newId;
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
<<<<<<< HEAD
        if (updatedEpic != null && epics.containsKey(updatedEpic.getId())) {
            Epic epic = epics.get(updatedEpic.getId());
            epic.setName(updatedEpic.getName());
            epic.setDescription(updatedEpic.getDescription());
=======
        requireNonNull(updatedEpic, "Epic cannot be null");
        Epic existingEpic = epics.get(updatedEpic.getId());
        if (existingEpic != null) {
            existingEpic.setName(updatedEpic.getName());
            existingEpic.setDescription(updatedEpic.getDescription());
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
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
<<<<<<< HEAD
        return subtasks.values().stream()
                .map(this::copySubtask)
                .collect(Collectors.toList());
=======
        return new ArrayList<>(subtasks.values());
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
<<<<<<< HEAD
            return copySubtask(subtask);
=======
            return new Subtask(subtask.getId(), subtask.getName(),
                    subtask.getDescription(), subtask.getStatus(),
                    subtask.getEpicId());
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
        }
        return null;
    }

    @Override
<<<<<<< HEAD
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
=======
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
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
    }

    @Override
    public void updateSubtask(Subtask updatedSubtask) {
<<<<<<< HEAD
        if (updatedSubtask != null && subtasks.containsKey(updatedSubtask.getId())) {
            Subtask subtask = subtasks.get(updatedSubtask.getId());
            subtask.setName(updatedSubtask.getName());
            subtask.setDescription(updatedSubtask.getDescription());
            subtask.setStatus(updatedSubtask.getStatus());
            updateEpicStatus(subtask.getEpicId());
=======
        requireNonNull(updatedSubtask, "Subtask cannot be null");
        if (subtasks.containsKey(updatedSubtask.getId())) {
            subtasks.put(updatedSubtask.getId(), updatedSubtask);
            updateEpicStatus(updatedSubtask.getEpicId());
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
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
<<<<<<< HEAD
                .map(this::copySubtask)
=======
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getHistory() {
<<<<<<< HEAD
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
=======
        return historyManager.getHistory();
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
<<<<<<< HEAD
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
=======
        if (epic != null) {
            List<Subtask> epicSubtasks = getSubtasksByEpicId(epicId);
            epic.updateStatus(epicSubtasks);
        }
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
    }
}