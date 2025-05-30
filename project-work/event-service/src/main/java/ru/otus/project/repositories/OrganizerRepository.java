package ru.otus.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.project.entities.Organizer;

/**
 * Репозиторий для работы с организаторами.
 */
public interface OrganizerRepository extends JpaRepository<Organizer, Long> {
    /**
     * Удаление организатора по идентификатору.
     *
     * @param id идентификатор удаляемого организатора.
     */
    @SuppressWarnings("all")
    @Modifying
    @Query(nativeQuery = true,
            value = "delete from entries " +
                    "where entries.event_id in (select id from events where events.organizer_id = :id);" +
                    "delete from reviews " +
                    "where reviews.event_id in (select id from events where events.organizer_id = :id);" +
                    "delete from events where organizer_id = :id;" +
                    "delete from organizer where id = :id")
    void deleteById(@Param(value = "id") Long id);
}
