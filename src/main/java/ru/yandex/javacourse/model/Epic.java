package ru.yandex.javacourse.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final List<Integer> subtaskIds = new ArrayList<>();
    private LocalDateTime startTime;
    private Duration duration;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public List<Integer> getSubtaskIds() {
        return new ArrayList<>(subtaskIds);
    }

    public void addSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(int subtaskId) {
        subtaskIds.remove((Integer) subtaskId);
    }

    public void clearSubtaskIds() {
        subtaskIds.clear();
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void updateTime(List<Subtask> subtasks) {
        if (subtasks.isEmpty()) {
            setStartTime(null);
            setDuration(null);
            setEndTime(null);
            return;
        }

        LocalDateTime minStartTime = null;
        LocalDateTime maxEndTime = null;
        Duration totalDuration = Duration.ZERO;

        for (Subtask subtask : subtasks) {
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

        setStartTime(minStartTime);
        setDuration(totalDuration);
        setEndTime(maxEndTime);
    }

    public void updateStatus(List<Subtask> subtasks) {
        if (subtasks.isEmpty()) {
            setStatus(Status.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subtask.getStatus() != Status.DONE) {
                allDone = false;
            }
        }

        if (allNew) {
            setStatus(Status.NEW);
        } else if (allDone) {
            setStatus(Status.DONE);
        } else {
            setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", endTime=" + endTime +
                ", subtaskIds=" + subtaskIds +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIds, epic.subtaskIds) &&
                Objects.equals(startTime, epic.startTime) &&
                Objects.equals(duration, epic.duration) &&
                Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds, startTime, duration, endTime);
    }
}