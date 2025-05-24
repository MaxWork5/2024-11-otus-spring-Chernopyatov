package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("all")
public interface CommentRepository extends MongoRepository<Comment, String> {
    Optional<Comment> findById(String id);

    List<Comment> findAllByBookId(String id);

    Comment save(Comment comment);

    void deleteById(String id);

    void deleteAllByBookId(String bookId);
}
