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
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Mongo для работы с комментариями ")
@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
@EnableAutoConfiguration(exclude = { StandardCommandsAutoConfiguration.class })
class MongoCommentRepositoryTest {
    @Autowired
    private MongoTemplate mongoTemplate;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private CommentRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @DisplayName("должен загружать комментарий по id")
    @Test
    void findById() {
        var savedComment = repository.save(new Comment("Comment_1", "67cc1d3bbdb9e25bc27de737"));
        var comment = repository.findById(savedComment.getId()).orElse(null);
        var query = Query.query(Criteria.where("_id").is(savedComment.getId()));
        var commentInBase = mongoTemplate.findOne(query, Comment.class);
        assertThat(comment).usingRecursiveComparison().isEqualTo(commentInBase);
    }

    @DisplayName("должен возвращать пустой Optional при отсутствие комментария по идентификатору")
    @Test
    void shouldReturnEmptyWhenCommentNotFoundById() {
        var comment = repository.findById("120L");
        assertThat(comment).isEmpty();
    }

    @DisplayName("должен загружать все комментарии принадлежащие одной книге")
    @Test
    void findAllByBookId() {
        var commentsForSaving = List.of(
                new Comment("Comment_1",
                        "67cc1d3bbdb9e25bc27de736"),
                new Comment("Comment_2",
                        "67cc1d3bbdb9e25bc27de737"),
                new Comment("Comment_3",
                        "67cc1d3bbdb9e25bc27de736"));
        repository.saveAll(commentsForSaving);
        var comments = repository.findAllByBookId("67cc1d3bbdb9e25bc27de736");
        assertThat(comments).isNotNull().hasSize(2)
                .allMatch(c -> !c.getDescription().isEmpty())
                .allMatch(c -> c.getBookId() != null);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void insert() {
        var commentsForSaving = List.of(
                new Comment("Comment_1",
                        "67cc1d3bbdb9e25bc27de736"),
                new Comment("Comment_2",
                        "67cc1d3bbdb9e25bc27de737"),
                new Comment("Comment_3",
                        "67cc1d3bbdb9e25bc27de736"));
        repository.saveAll(commentsForSaving);
        var commentsBeforeSave = repository.findAll();
        var savedComment = repository.save(new Comment("Test_Comment_1", "67cc1d3bbdb9e25bc27de738"));
        var commentsAfterSave = repository.findAll();
        var query = Query.query(Criteria.where("_id").is(savedComment.getId()));
        var baseComment = mongoTemplate.findOne(query, Comment.class);
        assertThat(commentsAfterSave.size()).isEqualTo(commentsBeforeSave.size() + 1);
        assertThat(savedComment).isNotNull().usingRecursiveComparison().isEqualTo(baseComment);
    }

    @DisplayName("должен сохранять изменения в комментарии")
    @Test
    void update() {
        var commentsForSaving = List.of(
                new Comment("Comment_1",
                        "67cc1d3bbdb9e25bc27de736"),
                new Comment("Comment_2",
                        "67cc1d3bbdb9e25bc27de737"),
                new Comment("Comment_3",
                        "67cc1d3bbdb9e25bc27de736"));
        repository.saveAll(commentsForSaving);
        var commentsBeforeSave = repository.findAll();
        var changedComment = new Comment("Test_Comment_1", "67cc1d3bbdb9e25bc27de738");
        changedComment.setId(commentsBeforeSave.get(commentsBeforeSave.size()-1).getId());
        var savedComment = repository.save(changedComment);
        var commentsAfterSave = repository.findAll();
        var query = Query.query(Criteria.where("_id").is(savedComment.getId()));
        var baseComment = mongoTemplate.findOne(query, Comment.class);
        assertThat(commentsAfterSave.size()).isEqualTo(commentsBeforeSave.size());
        assertThat(baseComment).isNotNull().isNotEqualTo(commentsBeforeSave.get(commentsBeforeSave.size()-1));
    }

    @DisplayName("должен удалить комментарий по id ")
    @Test
    void deleteById() {
        var commentsForSaving = List.of(
                new Comment("Comment_1",
                        "67cc1d3bbdb9e25bc27de736"),
                new Comment("Comment_2",
                        "67cc1d3bbdb9e25bc27de737"),
                new Comment("Comment_3",
                        "67cc1d3bbdb9e25bc27de736"));
        repository.saveAll(commentsForSaving);
        var commentsBeforeSave = repository.findAll();
        var beforeDelete = repository.findById(commentsBeforeSave.get(1).getId());
        repository.deleteById(commentsBeforeSave.get(1).getId());
        var commentsAfterSave = repository.findAll();
        var afterDeleting = repository.findById(commentsBeforeSave.get(1).getId());
        assertThat(commentsAfterSave.size()).isEqualTo(commentsBeforeSave.size() - 1);
        assertThat(beforeDelete).isNotNull();
        assertThat(afterDeleting).isNotNull().isEmpty();
    }

    @DisplayName("должен удалить комментарии по идентификатору книги ")
    @Test
    void deleteAllByBookId(){
        var commentsForSaving = List.of(
                new Comment("Comment_1",
                        "67cc1d3bbdb9e25bc27de736"),
                new Comment("Comment_2",
                        "67cc1d3bbdb9e25bc27de737"),
                new Comment("Comment_3",
                        "67cc1d3bbdb9e25bc27de736"));
        repository.saveAll(commentsForSaving);
        var commentsBeforeSave = repository.findAllByBookId("67cc1d3bbdb9e25bc27de736");
        repository.deleteAllByBookId("67cc1d3bbdb9e25bc27de736");
        var commentsAfterSave = repository.findAllByBookId("67cc1d3bbdb9e25bc27de736");
        assertThat(commentsBeforeSave).isNotEmpty();
        assertThat(commentsAfterSave).isEmpty();
    }
}