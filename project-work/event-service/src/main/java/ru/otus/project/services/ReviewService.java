package ru.otus.project.services;

import ru.otus.project.entities.Review;

import java.util.List;

/**
 * Сервис по работе с отзывами.
 */
public interface ReviewService {
    /**
     * Добавление отзыва.
     *
     * @param comment комментарий.
     * @param eventId идентификатор события.
     * @return добавленный отзыв
     */
    Review insert(String comment, Long eventId);

    /**
     * Получение отзывов для событий.
     *
     * @param eventId идентификаторы событий.
     * @return список отзывов.
     */
    List<Review> findReviews(List<Long> eventId);

    /**
     * Получение всех отзывов.
     *
     * @return список отзывов.
     */
    List<Review> findAll();

    /**
     * Получение отзыва по идентификатору.
     *
     * @return отзыв.
     */
    Review findById(Long id);

    /**
     * Обновление информации об отзыве.
     *
     * @param id      идентификатор отзыва.
     * @param comment комментарий.
     * @return обновленный отзыв.
     */
    Review update(Long id, String comment);

    /**
     * Удаление информации об отзыве по идентификатору.
     *
     * @param id идентификатор отзыва.
     */
    void delete(Long id);
}
