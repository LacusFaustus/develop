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
import ru.yandex.javacourse.model.Subtask;
import ru.yandex.javacourse.model.Task;
import ru.yandex.javacourse.util.Managers;

public class InMemoryTaskManager implements TaskManager
{
    protected int nextId = 1;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final Set<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(Task::getStartTime,
                            Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparingInt(Task::getId)
    );

    @Override
    public List<Task> getAllTasks()
    {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Task getTaskById(int id)
    {
        Task task = tasks.get(id);
        if (task != null)
        {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public int createTask(Task task)
    {
        validateTaskTime(task);
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        addToPrioritized(task);
        return task.getId();
    }

    @Override
    public void updateTask(Task updatedTask)
    {
        if (!tasks.containsKey(updatedTask.getId()))
        {
            return;
        }
        validateTaskTime(updatedTask);
        removeFromPrioritized(tasks.get(updatedTask.getId()));
        tasks.put(updatedTask.getId(), updatedTask);
        addToPrioritized(updatedTask);
    }

    @Override
    public void deleteTaskById(int id)
    {
        Task task = tasks.remove(id);
        if (task != null)
        {
            removeFromPrioritized(task);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteAllTasks()
    {
        tasks.values().forEach(task ->
        {
            removeFromPrioritized(task);
            historyManager.remove(task.getId());
        });
        tasks.clear();
    }

    @Override
    public List<Epic> getAllEpics()
    {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Epic getEpicById(int id)
    {
        Epic epic = epics.get(id);
        if (epic != null)
        {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public int createEpic(Epic epic)
    {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public void updateEpic(Epic updatedEpic)
    {
        Epic epic = epics.get(updatedEpic.getId());
        if (epic == null)
        {
            return;
        }
        epic.setName(updatedEpic.getName());
        epic.setDescription(updatedEpic.getDescription());
    }

    @Override
    public void deleteEpicById(int id)
    {
        Epic epic = epics.remove(id);
        if (epic != null)
        {
            for (Integer subtaskId : epic.getSubtaskIds())
            {
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteAllEpics()
    {
        subtasks.values().forEach(subtask ->
        {
            removeFromPrioritized(subtask);
            historyManager.remove(subtask.getId());
        });
        subtasks.clear();
        epics.values().forEach(epic -> historyManager.remove(epic.getId()));
        epics.clear();
    }

    @Override
    public List<Subtask> getAllSubtasks()
    {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Subtask getSubtaskById(int id)
    {
        Subtask subtask = subtasks.get(id);
        if (subtask != null)
        {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public int createSubtask(Subtask subtask)
    {
        validateTaskTime(subtask);
        if (!epics.containsKey(subtask.getEpicId()))
        {
            throw new ManagerSaveException("Epic not found for subtask");
        }
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtaskId(subtask.getId());
        updateEpicStatus(epic);
        updateEpicTime(epic.getId());
        addToPrioritized(subtask);
        return subtask.getId();
    }

    @Override
    public void updateSubtask(Subtask updatedSubtask)
    {
        Subtask subtask = subtasks.get(updatedSubtask.getId());
        if (subtask == null)
        {
            return;
        }
        validateTaskTime(updatedSubtask);
        removeFromPrioritized(subtask);
        subtasks.put(updatedSubtask.getId(), updatedSubtask);
        Epic epic = epics.get(updatedSubtask.getEpicId());
        updateEpicStatus(epic);
        updateEpicTime(epic.getId());
        addToPrioritized(updatedSubtask);
    }

    @Override
    public void deleteSubtaskById(int id)
    {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            removeFromPrioritized(subtask);
            Epic epic = epics.get(subtask.getEpicId());
            epic.removeSubtaskId(id);
            updateEpicStatus(epic);
            updateEpicTime(epic.getId());
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteAllSubtasks()
    {
        subtasks.values().forEach(subtask ->
        {
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
    public List<Subtask> getSubtasksByEpicId(int epicId)
    {
        return subtasks.values().stream()
                .filter(subtask -> subtask.getEpicId() == epicId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getPrioritizedTasks()
    {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public List<Task> getHistory()
    {
        return historyManager.getHistory();
    }

    protected void updateEpicStatus(Epic epic)
    {
        List<Subtask> subtasks = getSubtasksByEpicId(epic.getId());
        epic.updateStatus(subtasks);
    }

    protected void updateEpicTime(int epicId)
    {
        Epic epic = epics.get(epicId);
        if (epic == null) return;

        List<Subtask> subtasks = getSubtasksByEpicId(epicId);
        if (subtasks.isEmpty())
        {
            epic.setStartTime(null);
            epic.setDuration(Duration.ZERO);
            epic.setEndTime(null);
            return;
        }

        LocalDateTime minStartTime = null;
        LocalDateTime maxEndTime = null;
        Duration totalDuration = Duration.ZERO;

        for (Subtask subtask : subtasks)
        {
            LocalDateTime start = subtask.getStartTime();
            LocalDateTime end = subtask.getEndTime();
            Duration subtaskDuration = subtask.getDuration();

            if (start != null)
            {
                if (minStartTime == null || start.isBefore(minStartTime))
                {
                    minStartTime = start;
                }
            }

            if (end != null)
            {
                if (maxEndTime == null || end.isAfter(maxEndTime))
                {
                    maxEndTime = end;
                }
            }

            if (subtaskDuration != null)
            {
                totalDuration = totalDuration.plus(subtaskDuration);
            }
        }

        epic.setStartTime(minStartTime);
        epic.setDuration(totalDuration);
        epic.setEndTime(maxEndTime);
    }

    protected void validateTaskTime(Task newTask)
    {
        if (newTask.getStartTime() == null) return;

        for (Task task : prioritizedTasks)
        {
            if (task.getId() == newTask.getId()) continue;
            if (isTimeOverlap(newTask, task))
            {
                throw new ManagerSaveException("Task time overlap detected");
            }
        }
    }

    protected boolean isTimeOverlap(Task task1, Task task2)
    {
        if (task1.getStartTime() == null || task2.getStartTime() == null) return false;

        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();

        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    protected void addToPrioritized(Task task)
    {
        if (task.getStartTime() != null)
        {
            prioritizedTasks.add(task);
        }
    }

    protected void removeFromPrioritized(Task task)
    {
        prioritizedTasks.remove(task);
    }
}