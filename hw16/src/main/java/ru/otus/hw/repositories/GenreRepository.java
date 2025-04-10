package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.hw.domain.Genre;

import java.util.List;
import java.util.Set;

@SuppressWarnings("all")
@RepositoryRestResource(path = "genres")
public interface GenreRepository extends JpaRepository<Genre, Integer> {
    List<Genre> findAll();

    @RestResource(path = "ids")
    List<Genre> findAllByIdIn(Set<Long> ids);
}