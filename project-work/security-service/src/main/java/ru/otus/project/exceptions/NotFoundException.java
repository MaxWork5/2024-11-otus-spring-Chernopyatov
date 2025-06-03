package ru.otus.project.exceptions;

/**
 * Ошибка при нахождении сущности.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
