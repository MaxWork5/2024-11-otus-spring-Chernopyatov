package ru.otus.project.exceptions;

/**
 * Ошибка: событие не найдено.
 */
public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String message) {
        super(message);
    }
}
