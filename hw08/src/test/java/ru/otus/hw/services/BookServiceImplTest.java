package ru.otus.hw.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.shell.boot.StandardCommandsAutoConfiguration;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("Интеграционный тест сервиса для работы с книгами")
@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
@EnableAutoConfiguration(exclude = {StandardCommandsAutoConfiguration.class, TransactionAutoConfiguration.class})
class BookServiceImplTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    BookRepository bookRepository;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    GenreRepository genreRepository;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    AuthorRepository authorRepository;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    BookServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BookServiceImpl(authorRepository, commentRepository, genreRepository, bookRepository);
        for (String authorsList : List.of("Author_1", "Author_2", "Author_3")) {
            authorRepository.save(new Author(authorsList));
        }
        for (String genresList : List.of("Genre_1", "Genre_2", "Genre_3", "Genre_4", "Genre_5", "Genre_6")) {
            genreRepository.save(new Genre(genresList));
        }
    }

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
        genreRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @SuppressWarnings("ConstantConditions")
    @DisplayName("должен загрузить DTO книги из Базы Данных по его идентификатору")
    @Test
    void findById() {
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
        bookRepository.saveAll(booksForSaving);
        var query = Query.query(Criteria.where("_id").is(booksForSaving.get(0).getId()));
        var baseBook = mongoTemplate.findOne(query, Book.class);
        assertThat(service.findById(booksForSaving.get(0).getId()))
                .isEqualTo(Optional.of(new BookDto(baseBook.getId(),
                        "BookTitle_1",
                        new AuthorDto(baseBook.getAuthor().getId(), "Author_1"),
                        List.of(new GenreDto(baseBook.getGenres().get(0).getId(), "Genre_1"),
                                new GenreDto(baseBook.getGenres().get(1).getId(), "Genre_2")))));
    }

    @DisplayName("должен загрузить все DTO книг")
    @Test
    void findAll() {
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
        bookRepository.saveAll(booksForSaving);
        var expectedBooks = bookRepository.findAll().stream().map(book ->
                new BookDto(
                        book.getId(),
                        book.getTitle(),
                        new AuthorDto(
                                book.getAuthor().getId(),
                                book.getAuthor().getFullName()),
                        book.getGenres().stream()
                                .map(genre -> new GenreDto(genre.getId(), genre.getName())).toList())).toList();

        assertThat(service.findAll()).isNotNull().usingRecursiveComparison().isEqualTo(expectedBooks);
    }

    @SuppressWarnings("ConstantConditions")
    @DisplayName("должен загрузить новую книгу в Базу Данных")
    @Test
    void insert() {
        var genreIds = genreRepository.findAllByNameIn(Set.of("Genre_1", "Genre_4")).stream().map(Genre::getId).collect(Collectors.toSet());
        var savedBook = service.insert("Test_Book_1", authorRepository.findByFullName("Author_2").orElse(null).getId(),
                genreIds);
        var baseBook = service.findById(savedBook.getId());
        assertThat(baseBook)
                .get()
                .isEqualTo(savedBook);
    }

    @DisplayName("должен отбросить ошибку при попытке загрузить книгу в Базу Данных")
    @Test
    void insertThrowEntityNotFoundException() {
        assertThatThrownBy(() -> service.insert("Test_Book_1", "2", Set.of("1L", "40L")))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("должен отбросить ошибку при попытке записать книгу в Базе Данных не записав жанры")
    @Test
    void insertThrowIllegalArgumentException() {
        assertThatThrownBy(() -> service.insert("Test_Book_1", "20", Set.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @SuppressWarnings("ConstantConditions")
    @DisplayName("должен изменить книгу в Базе Данных")
    @Test
    void update() {
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
        bookRepository.saveAll(booksForSaving);
        var query = Query.query(Criteria.where("_id").is(booksForSaving.get(1).getId()));
        var queryBook = mongoTemplate.findOne(query, Book.class);
        var savedBook = service.update(queryBook.getId(), "Test_Book_1", queryBook.getAuthor().getId(),
                Set.of(queryBook.getGenres().get(0).getId(), queryBook.getGenres().get(1).getId()));
        var baseBook = service.findById(queryBook.getId());
        assertThat(baseBook)
                .get()
                .isEqualTo(savedBook);
        assertThat(baseBook.orElse(null)).isNotEqualTo(booksForSaving.get(1));
    }

    @DisplayName("должен отбросить ошибку при попытке обновить книгу в Базе Данных")
    @Test
    void updateThrowEntityNotFoundException() {
        var authorId = Objects.requireNonNull(authorRepository.findByFullName("Author_1").orElse(null)).getId();
        assertThatThrownBy(() -> service.update("2", "Test_Book_1", authorId, Set.of("1L", "4L")))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("должен отбросить ошибку при попытке обновить книгу в Базе Данных не записав жанры")
    @Test
    void updateThrowIllegalArgumentException() {
        assertThatThrownBy(() -> service.update("2","Test_Book_1", "20", Set.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @SuppressWarnings("ConstantConditions")
    @DisplayName("должен удалить книгу в Базе Данных")
    @Test
    void deleteById() {
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
        bookRepository.saveAll(booksForSaving);
        var countBooksBefore = bookRepository.findAll().size();
        var query = Query.query(Criteria.where("_id").is(booksForSaving.get(2).getId()));
        var queryBook = mongoTemplate.findOne(query, Book.class);
        var beforeDeleteBook = service.findById(queryBook.getId());
        service.deleteById(queryBook.getId());
        var countBooksAfter = bookRepository.findAll().size();
        var afterDeleteBook = service.findById(queryBook.getId());
        assertThat(countBooksAfter).isEqualTo(countBooksBefore - 1);
        assertThat(beforeDeleteBook).isNotNull().isNotEmpty();
        assertThat(afterDeleteBook).isNotNull().isEmpty();
    }
}