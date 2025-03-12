package ru.otus.hw.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.shell.boot.StandardCommandsAutoConfiguration;
import ru.otus.hw.models.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Mongo для работы с авторами ")
@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
@EnableAutoConfiguration(exclude = { StandardCommandsAutoConfiguration.class })
class MongoAuthorRepositoryTest {
    @Autowired
    private MongoTemplate mongoTemplate;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private AuthorRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void findAll() {
        for (String authorsList : List.of("Author_1", "Author_2", "Author_3")) {
            repository.save(new Author(authorsList));
        }
        var authors = repository.findAll();
        assertThat(authors).isNotNull().hasSize(3)
                .allMatch(s -> !s.getFullName().isEmpty());
    }

    @DisplayName("должен загрузить автора по принадлежащему ему идентификатору")
    @Test
    void findById() {
        var expectedAuthor = repository.save(new Author("Author_1"));
        var optionalActualAuthor = repository.findById(expectedAuthor.getId());
        var query = Query.query(Criteria.where("_id").is(expectedAuthor.getId()));
        var baseAuthor = mongoTemplate.findOne(query, Author.class);
        assertThat(optionalActualAuthor).isPresent().get()
                .usingRecursiveComparison().isEqualTo(baseAuthor);
    }

    @DisplayName("должен загрузить автора по имени")
    @Test
    void findByFullName() {
        var expectedAuthor = repository.save(new Author("Author_1"));
        var optionalActualAuthor = repository.findByFullName("Author_1");
        var query = Query.query(Criteria.where("_id").is(expectedAuthor.getId()));
        var baseAuthor = mongoTemplate.findOne(query, Author.class);
        assertThat(optionalActualAuthor).isPresent().get()
                .usingRecursiveComparison().isEqualTo(baseAuthor);
    }
}