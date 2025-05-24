package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.domain.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с авторами ")
@DataJpaTest
class JpaAuthorRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private AuthorRepository authorRepository;

    @DisplayName("должен загружать список всех авторов")
    @Test
    void findAll() {
        var authors = authorRepository.findAll();
        assertThat(authors).isNotNull().hasSize(3)
                .allMatch(s -> !s.getFullName().isEmpty());
    }

    @DisplayName("должен загрузить автора по принадлежащему ему идентификатору")
    @Test
    void findById() {
        var optionalActualAuthor = authorRepository.findById(1);
        var expectedAuthor = em.find(Author.class, 1);
        assertThat(optionalActualAuthor).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedAuthor);
    }
}