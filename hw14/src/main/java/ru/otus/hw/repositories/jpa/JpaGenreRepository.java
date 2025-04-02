package ru.otus.hw.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.entities.jpa.JpaGenre;

public interface JpaGenreRepository extends JpaRepository<JpaGenre, Integer> {
}
