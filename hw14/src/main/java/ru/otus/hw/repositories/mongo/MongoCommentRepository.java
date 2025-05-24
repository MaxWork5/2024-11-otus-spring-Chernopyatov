package ru.otus.hw.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.entities.mongo.MongoComment;

public interface MongoCommentRepository extends MongoRepository<MongoComment, String> {
}
