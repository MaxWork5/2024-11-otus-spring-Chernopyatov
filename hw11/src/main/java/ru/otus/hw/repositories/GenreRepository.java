package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.domain.Genre;

import java.util.Set;

@SuppressWarnings("all")
public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {
    Flux<Genre> findAll();

    Flux<Genre> findAllByIdIn(Set<String> ids);
}