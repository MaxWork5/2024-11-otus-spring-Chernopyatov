package ru.otus.project.exceptions;

/**
 * Ошибка: клиент не найден.
 */
public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String message) {
        super(message);
    }
}
