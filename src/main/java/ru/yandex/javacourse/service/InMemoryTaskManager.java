package ru.yandex.javacourse.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import ru.yandex.javacourse.exception.ManagerSaveException;
import ru.yandex.javacourse.model.Epic;
import ru.yandex.javacourse.model.Status;
import ru.yandex.javacourse.model.Subtask;
import ru.yandex.javacourse.model.Task;
import ru.yandex.javacourse.util.Managers;

/**
 * Реализация менеджера задач, хранящего данные в оперативной памяти.
 */
public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected int nextId = 1;
    protected HistoryManager historyManager;
    private Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public InMemoryTaskManager() {
        this(Managers.getDefaultHistory());
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Task getTaskById(final int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public int createTask(final Task task) {
        validateTaskTime(task);
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        addToPrioritized(task);
        return task.getId();
    }

    @Override
    public void updateTask(final Task updatedTask) {
        if (!tasks.containsKey(updatedTask.getId())) {
            return;
        }
        validateTaskTime(updatedTask);
        removeFromPrioritized(tasks.get(updatedTask.getId()));
        tasks.put(updatedTask.getId(), updatedTask);
        addToPrioritized(updatedTask);
    }

    @Override
    public void deleteTaskById(final int id) {
        Task task = tasks.remove(id);
        if (task != null) {
            removeFromPrioritized(task);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteAllTasks() {
        tasks.values().forEach(task -> {
            removeFromPrioritized(task);
            historyManager.remove(task.getId());
        });
        tasks.clear();
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Epic getEpicById(final int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public int createEpic(final Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public void updateEpic(final Epic updatedEpic) {
        Epic epic = epics.get(updatedEpic.getId());
        if (epic == null) {
            return;
        }
        epic.setName(updatedEpic.getName());
        epic.setDescription(updatedEpic.getDescription());
    }

    @Override
    public void deleteEpicById(final int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteAllEpics() {
        subtasks.values().forEach(subtask -> {
            removeFromPrioritized(subtask);
            historyManager.remove(subtask.getId());
        });
        subtasks.clear();
        epics.values().forEach(epic -> historyManager.remove(epic.getId()));
        epics.clear();
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Subtask getSubtaskById(final int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public int createSubtask(final Subtask subtask) {
        validateTaskTime(subtask);
        if (!epics.containsKey(subtask.getEpicId())) {
            throw new ManagerSaveException("Epic not found for subtask");
        }
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtaskId(subtask.getId());
        updateEpicStatus(epic); // Добавлен вызов
        updateEpicTime(epic.getId());
        addToPrioritized(subtask);
        return subtask.getId();
    }

    @Override
    public void updateSubtask(final Subtask updatedSubtask) {
        Subtask subtask = subtasks.get(updatedSubtask.getId());
        if (subtask == null) {
            return;
        }
        validateTaskTime(updatedSubtask);
        removeFromPrioritized(subtask);
        subtasks.put(updatedSubtask.getId(), updatedSubtask);
        Epic epic = epics.get(updatedSubtask.getEpicId());
        updateEpicStatus(epic); // Добавлен вызов
        updateEpicTime(epic.getId());
        addToPrioritized(updatedSubtask);
    }

    @Override
    public void deleteSubtaskById(final int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            removeFromPrioritized(subtask);
            Epic epic = epics.get(subtask.getEpicId());
            epic.removeSubtaskId(id);
            updateEpicStatus(epic); // Добавлен вызов
            updateEpicTime(epic.getId());
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.values().forEach(subtask -> {
            removeFromPrioritized(subtask);
            historyManager.remove(subtask.getId());
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtaskId(subtask.getId());
                updateEpicStatus(epic);
                updateEpicTime(epic.getId());
            }
        });
        subtasks.clear();
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(final int epicId) {
        return subtasks.values().stream()
                .filter(subtask -> subtask.getEpicId() == epicId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateEpicStatus(final Epic epic) {
        List<Subtask> epicSubtasks = getSubtasksByEpicId(epic.getId());
        if (epicSubtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (Subtask subtask : epicSubtasks) {
            if (subtask.getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subtask.getStatus() != Status.DONE) {
                allDone = false;
            }
        }

        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    protected void updateEpicTime(final int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }

        List<Subtask> epicSubtasks = getSubtasksByEpicId(epicId);
        if (epicSubtasks.isEmpty()) {
            epic.setStartTime(null);
            epic.setDuration(Duration.ZERO);
            epic.setEndTime(null);
            return;
        }

        LocalDateTime minStartTime = null;
        LocalDateTime maxEndTime = null;
        Duration totalDuration = Duration.ZERO;

        for (Subtask subtask : epicSubtasks) {
            LocalDateTime start = subtask.getStartTime();
            LocalDateTime end = subtask.getEndTime();
            Duration subtaskDuration = subtask.getDuration();

            if (start != null) {
                if (minStartTime == null || start.isBefore(minStartTime)) {
                    minStartTime = start;
                }
            }

            if (end != null) {
                if (maxEndTime == null || end.isAfter(maxEndTime)) {
                    maxEndTime = end;
                }
            }

            if (subtaskDuration != null) {
                totalDuration = totalDuration.plus(subtaskDuration);
            }
        }

        epic.setStartTime(minStartTime);
        epic.setDuration(totalDuration);
        epic.setEndTime(maxEndTime);
    }

    private void validateTaskTime(final Task newTask) {
        if (newTask.getStartTime() == null) {
            return;
        }

        for (Task task : prioritizedTasks) {
            if (task.getId() == newTask.getId()) {
                continue;
            }
            if (isTimeOverlap(newTask, task)) {
                throw new ManagerSaveException("Task time overlap detected");
            }
        }
    }

    private boolean isTimeOverlap(final Task task1, final Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null) {
            return false;
        }

        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();

        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    private void addToPrioritized(final Task task) {
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    private void removeFromPrioritized(final Task task) {
        prioritizedTasks.remove(task);
    }
}