package ru.otus.project.services;

import ru.otus.project.entities.Organizer;

import java.util.List;

/**
 * Сервис по работе с организаторами.
 */
public interface OrganizerService {
    /**
     * Получение информации о всех организаторах.
     *
     * @return список организаторов.
     */
    List<Organizer> findAll();

    /**
     * Получение информации об организаторе по идентификатору.
     *
     * @return информация об организаторе.
     */
    Organizer findById(Long id);

    /**
     * Добавление информации об организаторе.
     *
     * @param login логин организатора.
     * @return добавленная информация об организаторе.
     */
    Organizer insert(String login);

    /**
     * Обновление информации об организаторе.
     *
     * @param id    идентификатор организатора.
     * @param login логин организатора.
     * @return обновленная информация об организаторе.
     */
    Organizer update(Long id, String login);

    /**
     * Удаление информации об организаторе по идентификатору.
     *
     * @param id идентификатор организатора.
     */
    void delete(Long id);
}
