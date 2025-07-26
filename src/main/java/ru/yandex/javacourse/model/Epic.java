package ru.yandex.javacourse.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
<<<<<<< HEAD
    private final List<Integer> subtaskIds = new ArrayList<>();
=======
    private List<Integer> subtaskIds = new ArrayList<>();
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67

    public Epic(String name, String description) {
        super(name, description);
    }

<<<<<<< HEAD
    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    // Остальные методы остаются без изменений
=======
    public Epic(Epic other) {
        super(other.getId(), other.getName(), other.getDescription(), other.getStatus());
        this.subtaskIds = new ArrayList<>(other.subtaskIds);
    }

>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
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
<<<<<<< HEAD
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
=======
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
                ", status=" + getStatus() +
                ", subtaskIds=" + subtaskIds +
                '}';
    }
}