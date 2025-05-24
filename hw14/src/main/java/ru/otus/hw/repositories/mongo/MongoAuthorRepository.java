package ru.otus.hw.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.entities.mongo.MongoAuthor;

import java.util.Optional;

public interface MongoAuthorRepository extends MongoRepository<MongoAuthor, String> {
    Optional<MongoAuthor> findByFullName(String fullName);
}
