package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.domain.Genre;

import java.util.List;
import java.util.Set;

@SuppressWarnings("all")
public interface GenreRepository extends JpaRepository<Genre, Integer> {
    List<Genre> findAll();

    List<Genre> findAllByIdIn(Set<Long> ids);
}