package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.hw.domain.Book;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("all")
@RepositoryRestResource(path = "books")
public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(attributePaths = "author")
    @Query("select distinct b from Book b join fetch b.genres where b.id = :id")
    @RestResource(path = "ids")
    Optional<Book> findById(@Param("id") long id);

    @EntityGraph(attributePaths = "author")
    @Query("select distinct b from Book b join fetch b.genres")
    List<Book> findAll();

    Book save(@Nonnull Book book);

    void deleteById(long id);
}