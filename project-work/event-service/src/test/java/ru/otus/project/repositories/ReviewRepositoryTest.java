package ru.otus.project.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.otus.project.entities.Review;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("Тест репозитория отзывов")
@DataJpaTest
@Testcontainers(disabledWithoutDocker = true)
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository repository;

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:15-alpine");

    @DisplayName("Должен сохранить новый отзыв")
    @Sql(statements = {"insert into organizers values (1, 'testLogin')",
            "insert into events values ('2025-10-10', 1, 1, 'description', 'testName', 'PRESENTATION')"})
    @Test
    void save() {
        var review = new Review();
        review.setComment("testComment");
        review.setEventId(1L);

        var saved = repository.save(review);

        assertThat(saved.getEventId()).isNotNull();
        assertThat(saved.getComment()).isEqualTo(review.getComment());
        assertThat(saved.getEventId()).isEqualTo(review.getEventId());
    }

    @DisplayName("Должен найти отзыва по идентификатору события")
    @Sql(statements = {"insert into organizers values (1, 'testLogin')",
            "insert into events values ('2025-10-10', 1, 1, 'description', 'testName', 'PRESENTATION')",
            "insert into reviews values (1, 1, 'testComment')"
    })
    @Test
    void findAllByEventIdInOrderByEventId() {
        var review = new Review();
        review.setId(1L);
        review.setEventId(1L);
        review.setComment("testComment");

        assertThat(repository.findAllByEventIdInOrderByEventId(List.of(1L)))
                .usingRecursiveComparison()
                .isEqualTo(List.of(review));
    }

    @DisplayName("Должен удалить отзывы по идентификатору события")
    @Sql(statements = {"insert into organizers values (1, 'testLogin')",
            "insert into events values ('2025-10-10', 1, 1, 'description', 'testName', 'PRESENTATION')",
            "insert into reviews values (1, 1, 'testComment'), (2, 2, 'newTestComment')"
    })
    @Test
    void deleteAllByEventId(){
        var before = repository.count();
        repository.deleteAllByEventId(1L);
        var after = repository.count();

        assertThat(after).isEqualTo(before - 1);
    }

    @DisplayName("Должен удалить отзывы по списку идентификаторов события")
    @Sql(statements = {"insert into organizers values (1, 'testLogin')",
            "insert into events values ('2025-10-10', 1, 1, 'description', 'testName', 'PRESENTATION')",
            "insert into reviews values (1, 1, 'testComment'), (2, 2, 'newTestComment')"
    })
    @Test
    void deleteAllByEventIdIn(){
        var before = repository.count();
        repository.deleteAllByEventIdIn(List.of(1L, 2L));
        var after = repository.count();

        assertThat(after).isEqualTo(before - 2);
    }
}