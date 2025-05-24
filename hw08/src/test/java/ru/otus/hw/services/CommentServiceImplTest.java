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
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("Интеграционный тест сервиса для работы с комментариями")
@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
@EnableAutoConfiguration(exclude = {StandardCommandsAutoConfiguration.class, TransactionAutoConfiguration.class})
class CommentServiceImplTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    BookRepository bookRepository;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    CommentServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CommentServiceImpl(bookRepository, commentRepository);
        var booksForSaving = List.of(
                new Book("BookTitle_1",
                        null,
                        null));
        bookRepository.saveAll(booksForSaving);
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        bookRepository.deleteAll();
    }

    @SuppressWarnings("ConstantConditions")
    @DisplayName("должен загрузить DTO комментария из Базы Данных по его идентификатору")
    @Test
    void findById() {
        var commentsForSaving = List.of(
                new Comment("Comment_1",
                        "67cc1d3bbdb9e25bc27de736"),
                new Comment("Comment_2",
                        "67cc1d3bbdb9e25bc27de737"),
                new Comment("Comment_3",
                        "67cc1d3bbdb9e25bc27de736"));
        commentRepository.saveAll(commentsForSaving);
        var query = Query.query(Criteria.where("_id").is(commentsForSaving.get(0).getId()));
        var queryComments = mongoTemplate.findOne(query, Comment.class);
        assertThat(service.findById(queryComments.getId()))
                .isEqualTo(Optional.of(new CommentDto(queryComments.getId(),
                        "Comment_1",
                        "67cc1d3bbdb9e25bc27de736")));
    }

    @DisplayName("должен загрузить DTO комментариев из Базы Данных принадлежащих одной книге")
    @Test
    void findByBookId() {
        var commentsForSaving = List.of(
                new Comment("Comment_1",
                        "67cc1d3bbdb9e25bc27de736"),
                new Comment("Comment_2",
                        "67cc1d3bbdb9e25bc27de737"),
                new Comment("Comment_3",
                        "67cc1d3bbdb9e25bc27de736"));
        commentRepository.saveAll(commentsForSaving);
        var comment = service.findByBookId("67cc1d3bbdb9e25bc27de736");
        assertThat(comment).isNotNull();
        assertThat(comment.size() > 1).isTrue();
    }

    @DisplayName("должен отбросить ошибку при попытке найти DTO комментариев из Базы Данных принадлежащих одной книге")
    @Test
    void findByBookIdThrowException() {
        assertThatThrownBy(() ->service.findByBookId("100"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("должен создать запись комментария в Базе Данных")
    @Test
    void insert() {
        var books = bookRepository.findAll();
        var savedComment = service.insert("using_for_test", books.get(0).getId());
        var baseComment = service.findById(savedComment.getId());
        var query = Query.query(Criteria.where("_id").is(savedComment.getId()));
        var queryComments = mongoTemplate.findOne(query, Comment.class);
        assertThat(queryComments).isNotNull();
        assertThat(baseComment)
                .get()
                .isEqualTo(savedComment);
    }

    @DisplayName("должен отбросить ошибку при попытке загрузить комментарий в Базу Данных")
    @Test
    void insertThrowException() {
        assertThatThrownBy(() -> service.insert("Test_Book_1", "20"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("должен изменить запись комментария в Базе Данных")
    @Test
    void update() {
        var commentsForSaving = List.of(
                new Comment("Comment_1",
                        "67cc1d3bbdb9e25bc27de736"),
                new Comment("Comment_2",
                        "67cc1d3bbdb9e25bc27de737"),
                new Comment("Comment_3",
                        "67cc1d3bbdb9e25bc27de736"));
        commentRepository.saveAll(commentsForSaving);
        var books = bookRepository.findAll();
        var savedComment = service.update(commentsForSaving.get(1).getId(), "using_for_test", books.get(0).getId());
        var baseComment = service.findById(savedComment.getId());
        var query = Query.query(Criteria.where("_id").is(savedComment.getId()));
        var queryComments = mongoTemplate.findOne(query, Comment.class);
        assertThat(queryComments).isNotNull();
        assertThat(baseComment).isNotEqualTo(commentsForSaving.get(1));
        assertThat(baseComment)
                .get()
                .isEqualTo(savedComment);
    }

    @DisplayName("должен отбросить ошибку при попытке обновить комментарий в Базе Данных")
    @Test
    void updateThrowException() {
        assertThatThrownBy(() -> service.insert("Test_Book_1", "20"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("должен удалить запись комментария в Базе Данных")
    @Test
    void deleteById() {
        var commentsForSaving = List.of(
                new Comment("Comment_1",
                        "67cc1d3bbdb9e25bc27de736"),
                new Comment("Comment_2",
                        "67cc1d3bbdb9e25bc27de737"),
                new Comment("Comment_3",
                        "67cc1d3bbdb9e25bc27de736"));
        commentRepository.saveAll(commentsForSaving);
        var id = commentsForSaving.get(1).getId();
        var countCommentBefore = commentRepository.findAll().size();
        var beforeDeleteComment = service.findById(id);
        service.deleteById(id);
        var countCommentAfter = commentRepository.findAll().size();
        var afterDeleteComment = service.findById(id);
        assertThat(countCommentAfter).isEqualTo(countCommentBefore - 1);
        assertThat(beforeDeleteComment).isNotNull().isNotEmpty();
        assertThat(afterDeleteComment).isNotNull().isEmpty();
    }
}