package ru.otus.hw.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.entities.mongo.MongoBook;

import java.util.Optional;

public interface MongoBookRepository extends MongoRepository<MongoBook, String> {
    Optional<MongoBook> findByTitle(String title);
}
