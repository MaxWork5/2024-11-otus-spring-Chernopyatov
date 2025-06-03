package ru.otus.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.project.entities.Review;

import java.util.Collection;
import java.util.List;

/**
 * Репозиторий для работы с отзывами.
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {
    /**
     * Сохранение данных об отзыве.
     *
     * @param review данные об отзыве.
     * @return сохраненный отзыв.
     */
    @SuppressWarnings("all")
    Review save(Review review);

    /**
     * Получение списка отзывов для событий.
     *
     * @param eventIds идентификаторы событий.
     * @return список отзывов.
     */
    List<Review> findAllByEventIdInOrderByEventId(Collection<Long> eventIds);

    /**
     * Удаление всех отзывов события
     *
     * @param eventId идентификатор события
     */
    void deleteAllByEventId(Long eventId);

    /**
     * Удаление всех отзывов событий
     *
     * @param eventIds список идентификаторов событий
     */
    void deleteAllByEventIdIn(Collection<Long> eventIds);
}
