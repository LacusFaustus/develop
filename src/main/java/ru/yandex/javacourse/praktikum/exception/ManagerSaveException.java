// src/main/java/ru/yandex/praktikum/exception/ManagerSaveException.java
package ru.yandex.javacourse.praktikum.exception;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}