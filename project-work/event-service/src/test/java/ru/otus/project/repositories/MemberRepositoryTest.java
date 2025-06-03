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
import ru.otus.project.entities.Member;
import ru.otus.project.entities.Event;
import ru.otus.project.entities.enums.EventType;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("Тест репозитория пользователей")
@DataJpaTest
@Testcontainers(disabledWithoutDocker = true)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository repository;

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:15-alpine");

    @DisplayName("Должен сохранить нового пользователя")
    @Test
    void save() {
        var client = new Member();
        client.setLogin("testLogin");

        var saved = repository.save(client);

        assertThat(saved.getId())
                .isNotNull();
        assertThat(saved.getLogin())
                .isEqualTo(client.getLogin());
    }
}