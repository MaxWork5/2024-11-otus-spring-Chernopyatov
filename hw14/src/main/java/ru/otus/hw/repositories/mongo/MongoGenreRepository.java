package ru.otus.hw.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.entities.mongo.MongoGenre;

import java.util.Collection;
import java.util.List;

public interface MongoGenreRepository extends MongoRepository<MongoGenre, String> {
    List<MongoGenre> findAllByNameIn(Collection<String> names);
}
