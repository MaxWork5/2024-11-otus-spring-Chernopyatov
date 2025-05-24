package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(attributePaths = "author")
    @Query("select distinct b from Book b join fetch b.genres where b.id = :id")
    Optional<Book> findById(@Param("id") long id);

    @SuppressWarnings("all")
    @EntityGraph(attributePaths = "author")
    @Query("select distinct b from Book b join fetch b.genres")
    List<Book> findAll();

    @SuppressWarnings("all")
    Book save(@Nonnull Book book);

    void deleteById(long id);
}