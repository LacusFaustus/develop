package ru.yandex.javacourse.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Базовый класс для представления задачи в системе управления задачами.
 * Содержит основную информацию о задаче: идентификатор, название, описание,
 * статус, время начала и продолжительность выполнения.
 */
public class Task {
    /** Уникальный идентификатор задачи */
    private int id;
    /** Название задачи */
    private String name;
    /** Подробное описание задачи */
    private String description;
    /** Текущий статус выполнения задачи */
    private Status status;
    /** Время начала выполнения задачи */
    private LocalDateTime startTime;
    /** Продолжительность выполнения задачи */
    private Duration duration;

    /**
     * Конструктор для создания новой задачи.
     *
     * @param name название задачи
     * @param description описание задачи
     * @param status статус задачи (NEW, IN_PROGRESS, DONE)
     * @param startTime время начала выполнения (может быть null)
     * @param duration продолжительность выполнения
     */
    public Task(String name, String description, Status status,
                LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    /**
     * Конструктор для создания задачи с существующим ID.
     * Используется при загрузке задач из хранилища.
     *
     * @param id идентификатор задачи
     * @param name название задачи
     * @param description описание задачи
     * @param status статус задачи
     * @param startTime время начала выполнения
     * @param duration продолжительность выполнения
     */
    public Task(int id, String name, String description,
                Status status, LocalDateTime startTime,
                Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    /**
     * Конструктор для создания задачи без указания времени выполнения.
     *
     * @param id идентификатор задачи
     * @param name название задачи
     * @param description описание задачи
     * @param status статус задачи
     */
    public Task(int id, String name, String description, Status status) {
        this(id, name, description, status, null, Duration.ZERO);
    }

    /**
     * Конструктор копирования. Создает новую задачу на основе существующей.
     *
     * @param other задача, которую нужно скопировать
     */
    public Task(Task other) {
        this(other.id, other.name, other.description, other.status,
                other.startTime, other.duration);
    }

    // Геттеры и сеттеры

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    /**
     * Возвращает время завершения задачи.
     * Рассчитывается как время начала плюс продолжительность.
     *
     * @return время завершения или null, если время начала не задано
     */
    public LocalDateTime getEndTime() {
        if (startTime == null || duration == null) {
            return null;
        }
        return startTime.plus(duration);
    }

    /**
     * Возвращает тип задачи.
     * В базовом классе всегда возвращает "TASK".
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