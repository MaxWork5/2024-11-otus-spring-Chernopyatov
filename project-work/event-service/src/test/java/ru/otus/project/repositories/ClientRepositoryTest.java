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
import ru.otus.project.entities.Client;
import ru.otus.project.entities.Event;
import ru.otus.project.entities.enums.EventType;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("Тест репозитория пользователей")
@DataJpaTest
@Testcontainers(disabledWithoutDocker = true)
class ClientRepositoryTest {

    @Autowired
    private ClientRepository repository;

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:15-alpine");

    @DisplayName("Должен сохранить нового пользователя")
    @Test
    void save() {
        var client = new Client();
        client.setLogin("testLogin");

        var saved = repository.save(client);

        assertThat(saved.getId())
                .isNotNull();
        assertThat(saved.getLogin())
                .isEqualTo(client.getLogin());
    }

    @DisplayName("Должен найти пользователя по идентификатору")
    @Sql(statements = {"insert into organizer values (1, 'testLogin')",
            "insert into events values ('2025-10-10', 1, 1, 'description', 'testName', 'PRESENTATION')",
            "insert into clients values (1, 'testLogin')",
            "insert into entries values (1, 1)"})
    @Test
    void findByLogin() {
        var event = new Event();
        event.setId(1L);
        event.setName("testName");
        event.setDescription("description");
        event.setType(EventType.PRESENTATION);
        event.setDate(LocalDate.of(2025, 10, 10));
        event.setOrganizerId(1L);
        var client = new Client();
        client.setId(1L);
        client.setLogin("testLogin");
        client.setEvents(List.of(event));

        assertThat(repository.findAllByEventId(1L))
                .usingRecursiveComparison()
                .isEqualTo(List.of(client));
    }
}