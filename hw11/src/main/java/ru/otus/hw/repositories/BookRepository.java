package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.domain.Book;

@SuppressWarnings("all")
public interface BookRepository extends ReactiveMongoRepository<Book, String> {
    Mono<Book> findById(String id);

    Flux<Book> findAll();

    Mono<Book> save(Book book);

    Mono<Void> deleteById(String id);
}
