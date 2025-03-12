package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@SuppressWarnings("all")
public interface GenreRepository extends MongoRepository<Genre, String> {
    List<Genre> findAll();

    List<Genre> findAllByIdIn(Set<String> ids);

    List<Genre> findAllByNameIn(Collection<String> names);
}