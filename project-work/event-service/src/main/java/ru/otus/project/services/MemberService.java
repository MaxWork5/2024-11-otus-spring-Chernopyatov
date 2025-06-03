package ru.otus.project.services;

import ru.otus.project.entities.Member;
import ru.otus.project.entities.Event;

import java.util.List;

/**
 * Сервис по работе с клиентами.
 */
public interface MemberService {
    /**
     * Добавление информации о клиенте.
     *
     * @param login логин клиента.
     * @return добавленная информация.
     */
    Member insert(String login);

    /**
     * Регистрация на событие.
     *
     * @param id      идентификатор клиента.
     * @param idEvent идентификатор события.
     * @return информация по клиенту после регистрации на событие.
     */
    Member signUp(Long id, Long idEvent);

    /**
     * Получение списка событий клиента.
     *
     * @param id идентификатор клиента.
     * @return список событий.
     */
    List<Event> lookOnEvents(Long id);

    /**
     * Получение списка участников события.
     *
     * @param idEvent идентификатор события.
     * @return список клиентов.
     */
    List<Member> findMembersOfEvent(Long idEvent);

    /**
     * Удаление информации о клиенте.
     *
     * @param id идентификатор клиента.
     */
    void deleteMember(Long id);

    /**
     * Обновление информации о клиенте.
     *
     * @param id       идентификатор клиента.
     * @param login    логин клиента.
     * @param idEvents список идентификаторов событий.
     * @return обновленная информация.
     */
    Member updateMember(Long id, String login, List<Long> idEvents);

    /**
     * Получение списка всех клиентов.
     *
     * @return список клиентов.
     */
    List<Member> findAll();

    /**
     * Получение данных о клиенте по его идентификатору.
     *
     * @param id идентификатор клиента.
     * @return клиент.
     */
    Member findById(Long id);
}
