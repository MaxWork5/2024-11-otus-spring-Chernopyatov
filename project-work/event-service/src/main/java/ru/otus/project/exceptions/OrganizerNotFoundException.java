package ru.otus.project.exceptions;

/**
 * Ошибка: организатор не найден.
 */
public class OrganizerNotFoundException extends RuntimeException {
    public OrganizerNotFoundException(String message) {
        super(message);
    }
}
