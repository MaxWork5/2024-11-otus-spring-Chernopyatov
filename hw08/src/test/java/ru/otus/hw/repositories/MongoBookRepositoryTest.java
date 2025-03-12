package ru.otus.hw.repositories;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.shell.boot.StandardCommandsAutoConfiguration;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Mongo для работы с книгами ")
@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
@EnableAutoConfiguration(exclude = { StandardCommandsAutoConfiguration.class })
class MongoBookRepositoryTest {
    @Autowired
    private MongoTemplate mongoTemplate;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private AuthorRepository authorRepository;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private GenreRepository genreRepository;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private BookRepository repository;

    @BeforeEach
    void setUp() {
        for (String authorsList : List.of("Author_1", "Author_2", "Author_3")) {
            authorRepository.save(new Author(authorsList));
        }
        for (String genresList : List.of("Genre_1", "Genre_2", "Genre_3", "Genre_4", "Genre_5", "Genre_6")) {
            genreRepository.save(new Genre(genresList));
        }
    }

    @AfterEach
    void tearDown() {
        authorRepository.deleteAll();
        genreRepository.deleteAll();
        repository.deleteAll();
    }

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        var savedBook = repository.save(new Book("BookTitle_1",
                authorRepository.findByFullName("Author_1").orElse(null),
                genreRepository.findAllByNameIn(Set.of("Genre_1", "Genre_2"))));
        var books = repository.findById(savedBook.getId());
        var query = Query.query(Criteria.where("_id").is(savedBook.getId()));
        var expectedBook = mongoTemplate.findOne(query, Book.class);
        assertThat(books).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("должен возвращать пустой Optional при отсутствие книги по идентификатору")
    @Test
    void shouldReturnEmptyWhenBookNotFoundById() {
        var books = repository.findById("120");
        assertThat(books).isEmpty();
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var booksForSaving = List.of(
                new Book("BookTitle_1",
                        authorRepository.findByFullName("Author_1").orElse(null),
                        genreRepository.findAllByNameIn(Set.of("Genre_1", "Genre_2"))),
                new Book("BookTitle_2",
                        authorRepository.findByFullName("Author_2").orElse(null),
                        genreRepository.findAllByNameIn(Set.of("Genre_3", "Genre_4"))),
                new Book("BookTitle_3",
                        authorRepository.findByFullName("Author_3").orElse(null),
                        genreRepository.findAllByNameIn(Set.of("Genre_5", "Genre_6"))));
        repository.saveAll(booksForSaving);
        var books = repository.findAll();
        var expectedBook = mongoTemplate.findAll(Book.class);
        assertThat(books).isNotNull().hasSize(3)
                .allMatch(b -> !b.getTitle().isEmpty())
                .allMatch(b -> b.getAuthor().getFullName() != null)
                .allMatch(b -> b.getGenres() != null && !b.getGenres().isEmpty());
        assertThat(books).isNotNull().usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var booksBeforeSave = repository.findAll();
        var savedBook = repository.save(new Book("TestBook_1",
                authorRepository.findByFullName("Author_3").orElse(null),
                genreRepository.findAllByNameIn(Set.of("Genre_1", "Genre_2"))));
        var booksAfterSave = repository.findAll();
        var query = Query.query(Criteria.where("_id").is(savedBook.getId()));
        var expectedBook = mongoTemplate.findOne(query, Book.class);
        assertThat(booksAfterSave.size()).isEqualTo(booksBeforeSave.size() + 1);
        assertThat(savedBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("должен сохранять изменения в книге")
    @Test
    void shouldSaveUpdatedBook() {
        var booksForSaving = List.of(
                new Book("BookTitle_1",
                        authorRepository.findByFullName("Author_1").orElse(null),
                        genreRepository.findAllByNameIn(Set.of("Genre_1", "Genre_2"))),
                new Book("BookTitle_2",
                        authorRepository.findByFullName("Author_2").orElse(null),
                        genreRepository.findAllByNameIn(Set.of("Genre_3", "Genre_4"))),
                new Book("BookTitle_3",
                        authorRepository.findByFullName("Author_3").orElse(null),
                        genreRepository.findAllByNameIn(Set.of("Genre_5", "Genre_6"))));
        repository.saveAll(booksForSaving);
        var booksBeforeSave = repository.findAll();
        var changedBook = new Book(
                "TestBook_1",
                booksBeforeSave.get(booksBeforeSave.size() - 1).getAuthor(),
                booksBeforeSave.get(booksBeforeSave.size() - 1).getGenres());
        changedBook.setId(booksBeforeSave.get(booksBeforeSave.size() - 1).getId());
        var updatedBook = repository.save(changedBook);
        var booksAfterSave = repository.findAll();
        var query = Query.query(Criteria.where("_id").is(updatedBook.getId()));
        var expectedBook = mongoTemplate.findOne(query, Book.class);
        assertThat(booksAfterSave.size()).isEqualTo(booksBeforeSave.size());
        assertThat(expectedBook).isNotNull().usingRecursiveComparison().isNotEqualTo(booksBeforeSave.get(booksBeforeSave.size() - 1));
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        var booksForSaving = List.of(
                new Book("BookTitle_1",
                        authorRepository.findByFullName("Author_1").orElse(null),
                        genreRepository.findAllByNameIn(Set.of("Genre_1", "Genre_2"))),
                new Book("BookTitle_2",
                        authorRepository.findByFullName("Author_2").orElse(null),
                        genreRepository.findAllByNameIn(Set.of("Genre_3", "Genre_4"))),
                new Book("BookTitle_3",
                        authorRepository.findByFullName("Author_3").orElse(null),
                        genreRepository.findAllByNameIn(Set.of("Genre_5", "Genre_6"))));
        repository.saveAll(booksForSaving);
        var booksBeforeSave = repository.findAll();

        var book = repository.findById(booksBeforeSave.get(booksBeforeSave.size() - 1).getId());
        repository.deleteById(booksBeforeSave.get(booksBeforeSave.size() - 1).getId());
        var booksAfterSave = repository.findAll();
        var afterDeleting = repository.findById(booksBeforeSave.get(booksBeforeSave.size() - 1).getId());
        assertThat(booksAfterSave.size()).isEqualTo(booksBeforeSave.size() - 1);
        assertThat(book).isNotNull();
        assertThat(afterDeleting).isNotNull().isEmpty();
    }
}
