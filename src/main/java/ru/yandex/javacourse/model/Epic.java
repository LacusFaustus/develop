package main.java.ru.yandex.javacourse.model;

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