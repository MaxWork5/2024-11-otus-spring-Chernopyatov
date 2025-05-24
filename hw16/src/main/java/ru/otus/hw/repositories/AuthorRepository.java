package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.otus.hw.domain.Author;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("all")
@RepositoryRestResource(path = "authors")
public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findAll();

    @RestResource(path = "ids")
    Optional<Author> findById(long id);
}