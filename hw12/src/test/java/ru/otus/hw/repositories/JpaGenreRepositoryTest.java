package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с жанрами")
@DataJpaTest
class JpaGenreRepositoryTest {

    @Autowired
    private GenreRepository repository;

    @DisplayName("должен загружать список всех жанров")
    @Test
    void findAll() {
        var genre = repository.findAll();
        assertThat(genre).isNotNull().hasSize(6)
                .allMatch(s -> !s.getName().isEmpty());
    }

    @DisplayName("должен загрузить жанр по принадлежащему ему идентификатору")
    @Test
    void findAllByIds() {
        var genre = repository.findAllByIdIn(Set.of(1L, 2L, 3L));
        assertThat(genre).isNotNull().hasSize(3)
                .allMatch(s -> !s.getName().isEmpty());
    }
}