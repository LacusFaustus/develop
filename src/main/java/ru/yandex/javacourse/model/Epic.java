package ru.yandex.javacourse.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task
{
    private final List<Integer> subtaskIds = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description)
    {
        super(name, description, Status.NEW, null, Duration.ZERO);
    }

    public Epic(int id, String name, String description, Status status)
    {
        super(id, name, description, status, null, Duration.ZERO);
    }

    public Epic(String name, String description, Task baseTask)
    {
        super(name, description, baseTask.getStatus(), baseTask.getStartTime(), baseTask.getDuration());
    }

    public List<Integer> getSubtaskIds()
    {
        return new ArrayList<>(subtaskIds);
    }

    public LocalDateTime getEndTime()
    {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime)
    {
        this.endTime = endTime;
    }

    public void updateStatus(List<Subtask> subtasks)
    {
        if (subtasks.isEmpty())
        {
            status = Status.NEW;
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (Subtask subtask : subtasks)
        {
            if (subtask.getStatus() != Status.NEW)
            {
                allNew = false;
            }
            if (subtask.getStatus() != Status.DONE)
            {
                allDone = false;
            }
        }

        if (allNew)
        {
            status = Status.NEW;
        } else if (allDone)
        {
            status = Status.DONE;
        } else
        {
            status = Status.IN_PROGRESS;
        }
    }

    public void calculateTime(List<Subtask> subtasks)
    {
        if (subtasks.isEmpty())
        {
            startTime = null;
            duration = Duration.ZERO;
            endTime = null;
            return;
        }

        LocalDateTime earliest = null;
        LocalDateTime latest = null;
        Duration totalDuration = Duration.ZERO;

        for (Subtask subtask : subtasks)
        {
            LocalDateTime start = subtask.getStartTime();
            LocalDateTime end = subtask.getEndTime();

            if (start != null)
            {
                if (earliest == null || start.isBefore(earliest))
                {
                    earliest = start;
                }
                if (end != null && (latest == null || end.isAfter(latest)))
                {
                    latest = end;
                }
                totalDuration = totalDuration.plus(subtask.getDuration());
            }
        }

        startTime = earliest;
        duration = totalDuration;
        endTime = latest;
    }

    public void addSubtaskId(int subtaskId)
    {
        if (!subtaskIds.contains(subtaskId))
        {
            subtaskIds.add(subtaskId);
        }
    }

    public void removeSubtaskId(int subtaskId)
    {
        subtaskIds.remove((Integer) subtaskId);
    }

    @Override
    public String getType()
    {
        return "EPIC";
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        if (!super.equals(o))
        {
            return false;
        }
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIds, epic.subtaskIds);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), subtaskIds);
    }
}