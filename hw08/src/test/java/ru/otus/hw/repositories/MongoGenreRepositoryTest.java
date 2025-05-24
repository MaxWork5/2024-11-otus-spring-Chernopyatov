package ru.otus.hw.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.boot.StandardCommandsAutoConfiguration;
import ru.otus.hw.models.Genre;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Mongo для работы с жанрами")
@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
@EnableAutoConfiguration(exclude = { StandardCommandsAutoConfiguration.class })
class MongoGenreRepositoryTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private GenreRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void findAll() {
        for (String genresList : List.of("Genre_1", "Genre_2", "Genre_3", "Genre_4", "Genre_5", "Genre_6")) {
            repository.save(new Genre(genresList));
        }
        var genre = repository.findAll();
        assertThat(genre).isNotNull().hasSize(6)
                .allMatch(s -> !s.getName().isEmpty());
    }

    @DisplayName("должен загрузить жанр по принадлежащему ему идентификатору")
    @Test
    void findAllByIds() {
        for (String genresList : List.of("Genre_1", "Genre_2", "Genre_3", "Genre_4", "Genre_5", "Genre_6")) {
            repository.save(new Genre(genresList));
        }
        Set<String> ids = new HashSet<>();
        for(Genre genres: repository.findAll()){
            if (Arrays.asList("Genre_1", "Genre_2", "Genre_3").contains(genres.getName())) {
                ids.add(genres.getId());
            }
        }
        var genre = repository.findAllByIdIn(ids);
        assertThat(genre).isNotNull().hasSize(3)
                .allMatch(s -> !s.getName().isEmpty());
    }

    @DisplayName("должен загрузить жанр по его названию")
    @Test
    void findAllByNameIn() {
        for (String genresList : List.of("Genre_1", "Genre_2", "Genre_3", "Genre_4", "Genre_5", "Genre_6")) {
            repository.save(new Genre(genresList));
        }
        var genre = repository.findAllByNameIn(List.of("Genre_1", "Genre_2", "Genre_3"));
        assertThat(genre).isNotNull().hasSize(3)
                .allMatch(s -> !s.getName().isEmpty());
    }
}