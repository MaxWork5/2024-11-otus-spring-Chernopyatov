package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.domain.Comment;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("all")
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findById(long id);

    List<Comment> findAllByBookId(long id);

    Comment save(@Nonnull Comment comment);

    void deleteById(long id);

    void deleteAllByBookId(long bookId);
}