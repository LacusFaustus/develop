package ru.yandex.javacourse.service;

<<<<<<< HEAD
import ru.yandex.javacourse.model.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private static class Node {
        Task task;
        Node prev;
        Node next;

        Node(Task task) {
            this.task = task;
=======
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.yandex.javacourse.model.Task;

public class InMemoryHistoryManager implements HistoryManager {
    private static class Node {
        Task data;
        Node next;
        Node prev;

        Node(Task data) {
            this.data = data;
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
        }
    }

    private final Map<Integer, Node> historyMap = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public void add(Task task) {
        if (task == null) return;
<<<<<<< HEAD
        remove(task.getId());
        linkLast(task);
=======

        // Удаляем существующую задачу, если есть
        remove(task.getId());

        // Добавляем новую задачу в конец
        Node newNode = new Node(task);
        linkLast(newNode);
        historyMap.put(task.getId(), newNode);
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
    }

    @Override
    public void remove(int id) {
<<<<<<< HEAD
        Node node = historyMap.remove(id);
        if (node != null) {
            removeNode(node);
=======
        Node node = historyMap.get(id);
        if (node != null) {
            removeNode(node);
            historyMap.remove(id);
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
        }
    }

    @Override
    public List<Task> getHistory() {
<<<<<<< HEAD
        List<Task> history = new ArrayList<>();
        Node current = head;
        while (current != null) {
            history.add(current.task);
            current = current.next;
        }
        return history;
    }

    private void linkLast(Task task) {
        Node newNode = new Node(task);
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
        }
        tail = newNode;
        historyMap.put(task.getId(), newNode);
=======
        List<Task> result = new ArrayList<>();
        Node current = head;
        while (current != null) {
            result.add(current.data);
            current = current.next;
        }
        return result;
    }

    private void linkLast(Node node) {
        if (head == null) {
            head = node;
        } else {
            tail.next = node;
            node.prev = tail;
        }
        tail = node;
>>>>>>> 147c5b5df09fb44a9dc1b3691d55a84f96821b67
    }

    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }
}