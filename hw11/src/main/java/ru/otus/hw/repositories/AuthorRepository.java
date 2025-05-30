package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.domain.Author;

@SuppressWarnings("all")
public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {
    Flux<Author> findAll();

    Mono<Author> findById(String id);
}