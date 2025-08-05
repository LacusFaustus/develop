package ru.yandex.javacourse.service;

public class ManagerOverlapException extends RuntimeException {
    public ManagerOverlapException(String message) {
        super(message);
    }
}