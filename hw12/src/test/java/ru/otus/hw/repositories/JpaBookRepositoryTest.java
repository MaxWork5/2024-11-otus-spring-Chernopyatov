package ru.otus.hw.repositories;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.domain.Book;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с книгами ")
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class JpaBookRepositoryTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BookRepository repository;


    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        var books = repository.findById(1);
        var expectedBooks = em.find(Book.class, 1);
        assertThat(books).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedBooks);
    }

    @DisplayName("должен возвращать пустой Optional при отсутствие книги по идентификатору")
    @Test
    void shouldReturnEmptyWhenBookNotFoundById() {
        var books = repository.findById(120L);
        assertThat(books).isEmpty();
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        var books = repository.findAll();
        assertThat(books).isNotNull().hasSize(4)
                .allMatch(b -> !b.getTitle().isEmpty())
                .allMatch(b -> b.getAuthor().getFullName() != null)
                .allMatch(b -> b.getGenres() != null && !b.getGenres().isEmpty());

        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var booksBeforeSave = repository.findAll();
        var author = authorRepository.findById(3);
        var genres = genreRepository.findAllByIdIn(Set.of(1L,2L));
        var savedBook = repository.save(new Book(0, "Test_Book_1", author.get(), genres));
        var booksAfterSave = repository.findAll();
        var expectedBook = em.find(Book.class, savedBook.getId());
        assertThat(booksAfterSave.size()).isEqualTo(booksBeforeSave.size() + 1);
        assertThat(savedBook).isNotNull().isEqualTo(expectedBook);
    }

    @DisplayName("должен сохранять изменения в книге")
    @Test
    void shouldSaveUpdatedBook() {
        var booksBeforeSave = repository.findAll();
        var newAuthor = authorRepository.findById(3);
        var genres = genreRepository.findAllByIdIn(Set.of(1L,2L));
        var updatedBook = repository.save(new Book(1, "Test_Book_1", newAuthor.get(), genres));
        var booksAfterSave = repository.findAll();
        var expectedBook = em.find(Book.class, updatedBook.getId());
        assertThat(booksAfterSave.size()).isEqualTo(booksBeforeSave.size());
        assertThat(expectedBook).isNotNull().isNotEqualTo(booksBeforeSave);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        var booksBeforeSave = repository.findAll();
        var book = repository.findById(4);
        repository.deleteById(4);
        var booksAfterSave = repository.findAll();
        var afterDeleting = repository.findById(4);
        assertThat(booksAfterSave.size()).isEqualTo(booksBeforeSave.size() - 1);
        assertThat(book).isNotNull();
        assertThat(afterDeleting).isNotNull().isEmpty();
    }
}