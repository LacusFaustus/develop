package ru.yandex.javacourse.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс, представляющий подзадачу, которая входит в состав эпика.
 */
public final class Subtask extends Task {
    /** Идентификатор эпика, к которому относится подзадача. */
    private final int epicId;

    /**
     * Конструктор для создания новой подзадачи.
     *
     * @param name название подзадачи
     * @param description описание подзадачи
     * @param epicId идентификатор эпика
     * @param status статус подзадачи
     * @param startTime время начала выполнения
     * @param duration продолжительность выполнения
     */
    public Subtask(final String name, final String description, final int epicId,
                   final Status status, final LocalDateTime startTime,
                   final Duration duration) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    /**
     * Конструктор для создания подзадачи с существующим ID.
     *
     * @param id идентификатор подзадачи
     * @param name название подзадачи
     * @param description описание подзадачи
     * @param epicId идентификатор эпика
     * @param status статус подзадачи
     */
    public Subtask(final int id, final String name, final String description,
                   final int epicId, final Status status) {
        super(id, name, description, status, null, Duration.ZERO);
        this.epicId = epicId;
    }

    /**
     * Конструктор для создания подзадачи на основе существующей задачи.
     *
     * @param name название подзадачи
     * @param description описание подзадачи
     * @param epicId идентификатор эпика
     * @param baseTask базовая задача для копирования данных
     */
    public Subtask(final String name, final String description, final int epicId,
                   final Task baseTask) {
        super(name, description, baseTask.getStatus(),
                baseTask.getStartTime(), baseTask.getDuration());
        this.epicId = epicId;
    }

    /**
     * Возвращает идентификатор эпика, к которому относится подзадача.
     *
     * @return идентификатор эпика
     */
    public int getEpicId() {
        return epicId;
    }

    @Override
    public String getType() {
        return "SUBTASK";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}