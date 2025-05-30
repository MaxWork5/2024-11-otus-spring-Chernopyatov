package ru.otus.project.exceptions;

/**
 * Ошибка: клиент не найден.
 */
public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(String message) {
        super(message);
    }
}
