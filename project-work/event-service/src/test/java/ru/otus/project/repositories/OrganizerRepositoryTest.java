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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("Тест репозитория организаторов")
@DataJpaTest
@Testcontainers(disabledWithoutDocker = true)
class OrganizerRepositoryTest {

    @Autowired
    private OrganizerRepository repository;

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:15-alpine");

    @DisplayName("Должен удалить организатора по идентификатору")
    @Sql(statements = {"insert into organizer values (1, 'testLogin'), (2, 'forDelete')"})
    @Test
    void deleteById() {
        var before = repository.count();
        repository.deleteById(2L);
        var after = repository.count();

        assertThat(after).isEqualTo(before - 1);
    }
}