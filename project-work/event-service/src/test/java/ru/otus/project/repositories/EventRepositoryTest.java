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
import ru.otus.project.entities.Event;
import ru.otus.project.entities.enums.EventType;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("Тест репозитория событий")
@DataJpaTest
@Testcontainers(disabledWithoutDocker = true)
class EventRepositoryTest {

    @Autowired
    private EventRepository repository;

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:15-alpine");

    @DisplayName("Должен найти событие с датой после 2027-10-09")
    @Sql(statements = {"insert into organizers values (1, 'testLogin')",
            "insert into events values ('2027-10-10', 1, 1, 'description', 'testName', 'PRESENTATION')"})
    @Test
    void findAllByDateAfter() {
        var event = new Event();
        event.setId(1L);
        event.setName("testName");
        event.setDescription("description");
        event.setType(EventType.PRESENTATION);
        event.setDate(LocalDate.of(2027, 10, 10));
        event.setOrganizerId(1L);
        event.setMembers(List.of());

        assertThat(repository.findAllByDateAfter(LocalDate.of(2027, 10, 9)))
                .usingRecursiveComparison()
                .isEqualTo(List.of(event));
    }

    @DisplayName("Должен найти события между 2027-10-09 и 2027-11-10 с типом PRESENTATION")
    @Sql(statements = {"insert into organizers values (1, 'testLogin')",
            "insert into events values ('2027-10-10', 1, 1, 'description', 'testName', 'PRESENTATION')"})
    @Test
    void findAllByDateBetweenAndType() {
        var event = new Event();
        event.setId(1L);
        event.setName("testName");
        event.setDescription("description");
        event.setType(EventType.PRESENTATION);
        event.setDate(LocalDate.of(2027, 10, 10));
        event.setOrganizerId(1L);
        event.setMembers(List.of());
        var start = LocalDate.of(2027, 10, 9);
        var end = LocalDate.of(2027, 10, 11);

        assertThat(repository.findAllByDateBetweenAndType(start, end, EventType.PRESENTATION))
                .usingRecursiveComparison()
                .isEqualTo(List.of(event));
    }

    @DisplayName("Должен найти событие по идентификатору организатора")
    @Sql(statements = {"insert into organizers values (1, 'testLogin')",
            "insert into events values ('2027-10-10', 1, 1, 'description', 'testName', 'PRESENTATION')"})
    @Test
    void findAllByOrganizerId() {
        var event = new Event();
        event.setId(1L);
        event.setName("testName");
        event.setDescription("description");
        event.setType(EventType.PRESENTATION);
        event.setDate(LocalDate.of(2027, 10, 10));
        event.setOrganizerId(1L);
        event.setMembers(List.of());

        assertThat(repository.findAllByOrganizerId(1L))
                .usingRecursiveComparison()
                .isEqualTo(List.of(event));
    }
}