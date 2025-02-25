package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaCommentRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("Интеграционный тест сервиса для работы с комментариями")
@DataJpaTest(excludeAutoConfiguration = {TransactionAutoConfiguration.class})
@Import({JpaBookRepository.class, JpaCommentRepository.class})
class CommentServiceImplTest {
    @Autowired
    JpaBookRepository bookRepository;
    @Autowired
    JpaCommentRepository commentRepository;

    CommentServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CommentServiceImpl(bookRepository, commentRepository);
    }

    @DisplayName("должен загрузить DTO комментария из Базы Данных по его идентификатору")
    @Test
    void findById() {
        assertThat(service.findById(1))
                .isEqualTo(Optional.of(new CommentDto(1,
                        "Comment 1",
                        new BookDto(1,
                                "BookTitle_1",
                                new AuthorDto(1, "Author_1"),
                                List.of(new GenreDto(1, "Genre_1"), new GenreDto(2, "Genre_2"))))));
    }

    @DisplayName("должен загрузить DTO комментариев из Базы Данных принадлежащих одной книге")
    @Test
    void findByBookId() {
        var comment = service.findByBookId(1);
        assertThat(comment).isNotNull();
        assertThat(comment.size() > 1).isTrue();
    }

    @DisplayName("должен отбросить ошибку при попытке найти DTO комментариев из Базы Данных принадлежащих одной книге")
    @Test
    void findByBookIdThrowException() {
        assertThatThrownBy(() ->service.findByBookId(100))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("должен создать запись комментария в Базе Данных")
    @Test
    void insert() {
        var savedComment = service.insert("using_for_test", 2);
        var baseComment = service.findById(savedComment.getId());
        assertThat(baseComment)
                .get()
                .isEqualTo(savedComment);
    }

    @DisplayName("должен отбросить ошибку при попытке загрузить комментарий в Базу Данных")
    @Test
    void insertThrowException() {
        assertThatThrownBy(() -> service.insert("Test_Book_1", 20))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("должен изменить запись комментария в Базе Данных")
    @Test
    void update() {
        var savedComment = service.update(2, "using_for_test", 2);
        var baseComment = service.findById(savedComment.getId());
        assertThat(baseComment)
                .get()
                .isEqualTo(savedComment);
    }

    @DisplayName("должен отбросить ошибку при попытке обновить комментарий в Базе Данных")
    @Test
    void updateThrowException() {
        assertThatThrownBy(() -> service.insert("Test_Book_1", 20))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("должен удалить запись комментария в Базе Данных")
    @Test
    void deleteById() {
        var beforeDeleteComment = service.findById(4);
        service.deleteById(4);
        var afterDeleteComment = service.findById(4);
        assertThat(beforeDeleteComment).isNotNull().isNotEmpty();
        assertThat(afterDeleteComment).isNotNull().isEmpty();
    }
}