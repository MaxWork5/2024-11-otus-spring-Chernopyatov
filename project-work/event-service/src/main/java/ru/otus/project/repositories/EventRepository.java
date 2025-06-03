package ru.otus.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.project.entities.Event;
import ru.otus.project.entities.enums.EventType;

import java.time.LocalDate;
import java.util.List;

/**
 * Репозиторий для работы с событиями.
 */
public interface EventRepository extends JpaRepository<Event, Long> {
    /**
     * Получение списка событий после переданной даты.
     *
     * @param date дата, после которой необходимо получить события.
     * @return список событий.
     */
    List<Event> findAllByDateAfter(LocalDate date);

    /**
     * Получение списка событий для промежутка времени с определенным типом.
     *
     * @param begin дата, после которой необходимо получить события.
     * @param end   дата, до которой необходимо получить события.
     * @param type  тип событий.
     * @return список событий.
     */
    List<Event> findAllByDateBetweenAndType(LocalDate begin, LocalDate end, EventType type);

    /**
     * Получение списка событий по идентификатору организатора
     *
     * @param organizerId идентификатор организатора
     * @return список событий
     */
    List<Event> findAllByOrganizerId(Long organizerId);
}
