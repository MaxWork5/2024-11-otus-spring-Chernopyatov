package ru.otus.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.project.entities.Organizer;

/**
 * Репозиторий для работы с организаторами.
 */
public interface OrganizerRepository extends JpaRepository<Organizer, Long> {
}
