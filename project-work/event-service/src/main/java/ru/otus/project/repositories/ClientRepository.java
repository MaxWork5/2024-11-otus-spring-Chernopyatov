package ru.otus.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.project.entities.Client;

import java.util.List;

/**
 * Репозиторий для работы с клиентами.
 */
public interface ClientRepository extends JpaRepository<Client, Long> {
    /**
     * Сохранение данных клиента.
     *
     * @param client данные клиента.
     * @return сохраненные данные клиента.
     */
    @SuppressWarnings("all")
    Client save(Client client);

    /**
     * Получение списка клиентов для события.
     *
     * @param eventId идентификатор события.
     * @return список клиентов.
     */
    @Query(nativeQuery = true,
            value = "select c.id, c.login from clients as c inner join entries e on e.client_id = c.id " +
                    "where e.event_id = :eventId")
    List<Client> findAllByEventId(@Param(value = "eventId") Long eventId);
}
