package ru.yandex.javacourse.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.yandex.javacourse.model.Task;

/**
 * Реализация менеджера истории просмотров задач на основе двусвязного списка.
 */
public final class InMemoryHistoryManager implements HistoryManager {

    /**
     * Узел двусвязного списка для хранения задач.
     */
    private static final class Node {
        /** Задача, хранящаяся в узле. */
        private final Task task;
        /** Ссылка на предыдущий узел. */
        private Node prev;
        /** Ссылка на следующий узел. */
        private Node next;

        /**
         * Создает новый узел.
         *
         * @param task задача для хранения в узле
         */
        Node(final Task task) {
            this.task = task;
        }
    }

    /** Карта для быстрого доступа к узлам по ID задачи. */
    private final Map<Integer, Node> nodeMap = new HashMap<>();
    /** Первый узел в списке. */
    private Node head;
    /** Последний узел в списке. */
    private Node tail;

    @Override
    public void add(final Task task) {
        if (task == null) {
            return;
        }
        remove(task.getId());
        linkLast(new Task(task));
    }

    @Override
    public void remove(final int id) {
        Node node = nodeMap.remove(id);
        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        Node current = head;
        while (current != null) {
            history.add(current.task);
            current = current.next;
        }
        return history;
    }

    @Override
    public void clear() {
        nodeMap.clear();
        head = null;
        tail = null;
    }

    /**
     * Добавляет задачу в конец списка.
     *
     * @param task задача для добавления
     */
    private void linkLast(final Task task) {
        Node newNode = new Node(task);
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
        }
        tail = newNode;
        nodeMap.put(task.getId(), newNode);
    }

    /**
     * Удаляет узел из списка.
     *
     * @param node узел для удаления
     */
    private void removeNode(final Node node) {
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