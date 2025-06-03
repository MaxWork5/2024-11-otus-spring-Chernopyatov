package ru.otus.project.exceptions;

/**
 * Ошибка: отзыв не найден.
 */
public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(String message) {
        super(message);
    }
}
