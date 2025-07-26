package ru.yandex.javacourse.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task
{
    private final List<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description)
    {
        super(name, description);
    }

    public List<Integer> getSubtaskIds()
    {
        return new ArrayList<>(subtaskIds);
    }

    public void updateStatus(List<Subtask> subtasks) {
        if (subtasks.isEmpty()) {
            this.status = Status.NEW;
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
            this.status = Status.NEW;
        } else if (allDone) {
            this.status = Status.DONE;
        } else {
            this.status = Status.IN_PROGRESS;
        }
    }

    public void addSubtaskId(int subtaskId)
    {
        subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(int subtaskId)
    {
        subtaskIds.remove((Integer) subtaskId);
    }

    public void clearSubtaskIds()
    {
        subtaskIds.clear();
    }

    @Override
    public String toString()
    {
        return "Epic{"
                + "name='" + getName() + '\''
                + ", description='" + getDescription() + '\''
                + ", id=" + getId()
                + ", status=" + getStatus()
                + ", subtaskIds=" + subtaskIds
                + '}';
    }
}