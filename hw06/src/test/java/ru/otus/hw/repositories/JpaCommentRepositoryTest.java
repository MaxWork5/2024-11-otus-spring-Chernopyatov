package ru.otus.hw.repositories;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с комментариями ")
@DataJpaTest
@Import({JpaCommentRepository.class, JpaBookRepository.class, JpaAuthorRepository.class, JpaGenreRepository.class})
class JpaCommentRepositoryTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private JpaBookRepository bookRepository;
    @Autowired
    private JpaCommentRepository repository;

    @DisplayName("должен загружать комментарий по id")
    @Test
    void findById() {
        var comment = repository.findById(1);
        var commentInBase = em.find(Comment.class, 1);
        assertThat(comment).get()
                .usingRecursiveComparison().isEqualTo(commentInBase);
    }

    @DisplayName("должен возвращать пустой Optional при отсутствие комментария по идентификатору")
    @Test
    void shouldReturnEmptyWhenCommentNotFoundById() {
        var comment = repository.findById(120L);
        assertThat(comment).isEmpty();
    }

    @DisplayName("должен загружать все комментарии принадлежащие одной книге")
    @Test
    void findAllByBookId() {
        SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        var comments = repository.findAllByBookId(1);
        System.out.println(comments);
        assertThat(comments).isNotNull().hasSize(2)
                .allMatch(c -> !c.getDescription().isEmpty())
                .allMatch(c -> c.getBook().getTitle() != null)
                .allMatch(c -> c.getBook().getAuthor() != null)
                .allMatch(c -> c.getBook().getGenres() != null && !c.getBook().getGenres().isEmpty());

        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(2);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void insert() {
        var book = bookRepository.findById(1).get();
        var commentsBeforeSave = repository.findAllByBookId(book.getId());
        var savedComment = repository.save(new Comment(0, "Test_Comment_1", book));
        var commentsAfterSave = repository.findAllByBookId(book.getId());
        var baseComment = em.find(Comment.class, savedComment.getId());
        assertThat(commentsAfterSave.size()).isEqualTo(commentsBeforeSave.size() + 1);
        assertThat(savedComment).isNotNull().isEqualTo(baseComment);
    }

    @DisplayName("должен сохранять изменения в комментарии")
    @Test
    void update() {
        var newBook = bookRepository.findById(3).get();
        var commentsBeforeSave = repository.findAllByBookId(newBook.getId());
        var savedComment = repository.save(new Comment(3, "Test_Comment_1", newBook));
        var commentsAfterSave = repository.findAllByBookId(newBook.getId());
        var baseComment = em.find(Comment.class, savedComment.getId());
        assertThat(commentsAfterSave.size()).isEqualTo(commentsBeforeSave.size() + 1);
        assertThat(savedComment).isNotNull().isEqualTo(baseComment);
    }

    @DisplayName("должен удалить комментарий по id ")
    @Test
    void deleteById() {
        var book = bookRepository.findById(1).get();
        var commentsBeforeSave = repository.findAllByBookId(book.getId());
        var beforeDelete = repository.findById(1);
        repository.deleteById(1);
        var commentsAfterSave = repository.findAllByBookId(book.getId());
        var afterDeleting = repository.findById(1);
        assertThat(commentsAfterSave.size()).isEqualTo(commentsBeforeSave.size() - 1);
        assertThat(beforeDelete).isNotNull();
        assertThat(afterDeleting).isNotNull().isEmpty();
    }
}