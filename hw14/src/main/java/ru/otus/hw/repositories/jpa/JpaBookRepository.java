package ru.otus.hw.repositories.jpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.hw.entities.jpa.JpaBook;

import java.util.List;

public interface JpaBookRepository extends JpaRepository<JpaBook, Long> {
    @SuppressWarnings("all")
    @EntityGraph(attributePaths = "author")
    @Query("select distinct b from JpaBook b join fetch b.genres")
    List<JpaBook> findAll();
}
