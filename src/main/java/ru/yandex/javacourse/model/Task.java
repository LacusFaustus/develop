package ru.yandex.javacourse.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task
{
    protected int id;
    protected String name;
    protected String description;
    protected Status status;
    protected LocalDateTime startTime;
    protected Duration duration;

    public Task(String name, String description, Status status,
                LocalDateTime startTime, Duration duration)
    {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(int id, String name, String description, Status status,
                LocalDateTime startTime, Duration duration)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(int id, String name, String description, Status status)
    {
        this(id, name, description, status, null, Duration.ZERO);
    }

    public Task(Task other)
    {
        this(other.id, other.name, other.description, other.status,
                other.startTime, other.duration);
    }

    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public Status getStatus()
    {
        return status;
    }
    public void setStatus(Status status)
    {
        this.status = status;
    }
    public LocalDateTime getStartTime()
    {
        return startTime;
    }
    public void setStartTime(LocalDateTime startTime)
    {
        this.startTime = startTime;
    }
    public Duration getDuration()
    {
        return duration;
    }
    public void setDuration(Duration duration)
    {
        this.duration = duration;
    }

    public LocalDateTime getEndTime()
    {
        if (startTime == null || duration == null)
        {
            return null;
        }
        return startTime.plus(duration);
    }

    public String getType()
    {
        return "TASK";
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id &&
                Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                status == task.status &&
                Objects.equals(startTime, task.startTime) &&
                Objects.equals(duration, task.duration);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, name, description, status, startTime, duration);
    }
}