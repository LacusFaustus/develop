package ru.yandex.javacourse.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Базовый класс для представления задачи.
 */
public class Task {
    /** Уникальный идентификатор задачи. */
    private int id;
    /** Название задачи. */
    private String name;
    /** Описание задачи. */
    private String description;
    /** Статус задачи. */
    protected Status status;
    /** Время начала выполнения задачи. */
    private LocalDateTime startTime;
    /** Продолжительность выполнения задачи. */
    private Duration duration;

    /**
     * Конструктор для создания новой задачи.
     *
     * @param name название задачи
     * @param description описание задачи
     * @param status статус задачи
     * @param startTime время начала выполнения
     * @param duration продолжительность выполнения
     */
    public Task(final String name, final String description, final Status status,
                final LocalDateTime startTime, final Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    /**
     * Конструктор для создания задачи с существующим ID.
     *
     * @param id идентификатор задачи
     * @param name название задачи
     * @param description описание задачи
     * @param status статус задачи
     * @param startTime время начала выполнения
     * @param duration продолжительность выполнения
     */
    public Task(final int id, final String name, final String description,
                final Status status, final LocalDateTime startTime,
                final Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    /**
     * Конструктор для создания задачи без времени выполнения.
     *
     * @param id идентификатор задачи
     * @param name название задачи
     * @param description описание задачи
     * @param status статус задачи
     */
    public Task(final int id, final String name, final String description,
                final Status status) {
        this(id, name, description, status, null, Duration.ZERO);
    }

    /**
     * Конструктор копирования.
     *
     * @param other задача для копирования
     */
    public Task(final Task other) {
        this(other.id, other.name, other.description, other.status,
                other.startTime, other.duration);
    }

    /**
     * Возвращает идентификатор задачи.
     *
     * @return идентификатор задачи
     */
    public int getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор задачи.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Возвращает название задачи.
     *
     * @return название задачи
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает название задачи.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает описание задачи.
     *
     * @return описание задачи
     */
    public String getDescription() {
        return description;
    }

    /**
     * Устанавливает описание задачи.
     */
    public void setDescription(String Description) {
        this.description = Description;
    }

    /**
     * Возвращает статус задачи.
     *
     * @return статус задачи
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Устанавливает статус задачи.
     */
    public void setStatus(Status Status) {
        this.status = Status;
    }

    /**
     * Возвращает время начала выполнения задачи.
     *
     * @return время начала
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Устанавливает время начала выполнения задачи.
     */
    public void setStartTime(LocalDateTime StartTime) {
        this.startTime = StartTime;
    }

    /**
     * Возвращает продолжительность выполнения задачи.
     *
     * @return продолжительность
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     * Устанавливает продолжительность выполнения задачи.
     */
    public void setDuration(Duration Duration) {
        this.duration = Duration;
    }

    /**
     * Возвращает время завершения задачи.
     *
     * @return время завершения или null, если задача не запланирована
     */
    public LocalDateTime getEndTime() {
        if (startTime == null || duration == null) {
            return null;
        }
        return startTime.plus(duration);
    }

    /**
     * Возвращает тип задачи.
     *
     * @return строковое представление типа задачи
     */
    public String getType() {
        return "TASK";
    }

    @Override
    public boolean equals(Object o) {
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
    public int hashCode() {
        return Objects.hash(id, name, description, status, startTime, duration);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }
}