package ru.otus.project.services;

import ru.otus.project.entities.Event;
import ru.otus.project.entities.UpdateEvent;
import ru.otus.project.entities.enums.EventType;

import java.time.LocalDate;
import java.util.List;

/**
 * Сервис по работе с событиями.
 */
public interface EventService {
    /**
     * Получение списка событий, доступных для регистрации.
     *
     * @return список событий.
     */
    List<Event> findEventsForSignUp();

    /**
     * Получение списка событий по критериям.
     *
     * @param startDate дата, после которой необходимо получить события.
     * @param endDate   дата, до которой необходимо получить события.
     * @param type      тип события.
     * @return список событий.
     */
    List<Event> findByDatesAndType(LocalDate startDate, LocalDate endDate, EventType type);

    /**
     * Добавление события.
     *
     * @param name        наименование события.
     * @param description описание события.
     * @param date        дата события.
     * @param type        тип события.
     * @param organizerId идентификатор организатора события.
     * @return добавленное событие.
     */
    Event insert(String name, String description, LocalDate date, EventType type, Long organizerId);

    /**
     * Обновление события.
     *
     * @param updateEvent данные для обновления события.
     * @return обновленное событие.
     */
    Event update(UpdateEvent updateEvent);

    /**
     * Получение списка всех событий.
     *
     * @return список событий.
     */
    List<Event> findAll();

    /**
     * Удаление события по идентификатору.
     *
     * @param id идентификатор события.
     */
    void delete(Long id);
}
