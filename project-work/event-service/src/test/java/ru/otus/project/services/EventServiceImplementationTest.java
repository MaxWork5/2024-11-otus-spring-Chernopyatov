package ru.otus.project.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.project.entities.Event;
import ru.otus.project.entities.UpdateEvent;
import ru.otus.project.entities.enums.EventType;
import ru.otus.project.exceptions.EventNotFoundException;
import ru.otus.project.exceptions.OrganizerNotFoundException;
import ru.otus.project.repositories.EventRepository;
import ru.otus.project.repositories.OrganizerRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@DisplayName("Тест сервиса событий")
@ExtendWith(MockitoExtension.class)
class EventServiceImplementationTest {
    @Mock
    private EventRepository repository;

    @Mock
    private OrganizerRepository organizerRepository;

    @InjectMocks
    private EventServiceImplementation service;

    private static Event event() {
        var event = new Event();
        event.setId(1L);
        event.setDate(LocalDate.of(2025, 1, 1));
        event.setDescription("This is a test");
        event.setName("testevent");
        event.setType(EventType.CORPORATE);
        event.setOrganizerId(1L);
        return event;
    }

    @DisplayName("Должен вернуть события с сегодняшнего дня")
    @Test
    void findEventsAfterToday() {
        var waited = new Event();
        waited.setId(1L);
        waited.setDate(LocalDate.of(2025, 1, 1));
        waited.setDescription("This is a test");
        waited.setName("testevent");
        waited.setType(EventType.CORPORATE);
        waited.setOrganizerId(1L);

        doReturn(List.of(event())).when(repository).findAllByDateAfter(any());

        var result = service.findEventsForSignUp();

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(waited));
    }

    @DisplayName("Должен вернуть события определённого типа в определённом временном промежутке")
    @Test
    void findByDatesAndType() {
        var start = LocalDate.of(2025, 1, 1);
        var end = LocalDate.of(2025, 1, 2);
        var waited = new Event();
        waited.setId(1L);
        waited.setDate(LocalDate.of(2025, 1, 1));
        waited.setDescription("This is a test");
        waited.setName("testevent");
        waited.setType(EventType.CORPORATE);
        waited.setOrganizerId(1L);

        doReturn(List.of(event())).when(repository).findAllByDateBetweenAndType(any(), any(), any());

        var result = service.findByDatesAndType(start, end, EventType.CORPORATE);

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(waited));
    }

    @DisplayName("Должен создать событие")
    @Test
    void insert() {
        var date = LocalDate.of(2025, 1, 1);
        var waited = new Event();
        waited.setId(1L);
        waited.setDate(LocalDate.of(2025, 1, 1));
        waited.setDescription("This is a test");
        waited.setName("testevent");
        waited.setType(EventType.CORPORATE);
        waited.setOrganizerId(1L);

        doReturn(true).when(organizerRepository).existsById(1L);
        doReturn(event()).when(repository).save(any());

        var result = service.insert("testevent", "This is a test", date, EventType.CORPORATE, 1L);

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(waited);
    }

    @DisplayName("Должен выбросить ошибку, что организатор не найден при создании пользователя")
    @Test
    void insert_OrganizerNotFound() {
        var date = LocalDate.of(2025, 1, 1);
        doReturn(false).when(organizerRepository).existsById(1L);

        assertThatThrownBy(() -> service.insert("testevent", "This is a test", date, EventType.CORPORATE, 1L))
                .isInstanceOf(OrganizerNotFoundException.class);
    }

    @DisplayName("Должен изменить событие")
    @Test
    void update() {
        var date = LocalDate.of(2025, 1, 1);
        var waited = new Event();
        waited.setId(1L);
        waited.setDate(LocalDate.of(2025, 1, 1));
        waited.setDescription("This is a test");
        waited.setName("testevent");
        waited.setType(EventType.CORPORATE);
        waited.setOrganizerId(1L);

        doReturn(true).when(organizerRepository).existsById(1L);
        doReturn(true).when(repository).existsById(1L);
        doReturn(event()).when(repository).save(any());

        var result = service.update(new UpdateEvent(1L,
                "testevent", "This is a test", date, EventType.CORPORATE, 1L));

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(waited);
    }

    @DisplayName("Должен выбросить ошибку, что организатор не найден при изменении пользователя")
    @Test
    void update_OrganizerNotFound() {
        var date = LocalDate.of(2025, 1, 1);

        doReturn(false).when(organizerRepository).existsById(1L);

        assertThatThrownBy(() -> service.update(new UpdateEvent(1L,
                        "testevent", "This is a test", date, EventType.CORPORATE, 1L)))
                .isInstanceOf(OrganizerNotFoundException.class);
    }

    @DisplayName("Должен выбросить ошибку, что событие не найдено при изменении пользователя")
    @Test
    void update_EventNotFound() {
        var date = LocalDate.of(2025, 1, 1);

        doReturn(true).when(organizerRepository).existsById(1L);
        doReturn(false).when(repository).existsById(1L);

        assertThatThrownBy(() -> service.update(new UpdateEvent(1L,
                "testevent", "This is a test", date, EventType.CORPORATE, 1L)))
                .isInstanceOf(EventNotFoundException.class);
    }

    @DisplayName("Должен вернуть все события")
    @Test
    void findAll() {
        var waited = new Event();
        waited.setId(1L);
        waited.setDate(LocalDate.of(2025, 1, 1));
        waited.setDescription("This is a test");
        waited.setName("testevent");
        waited.setType(EventType.CORPORATE);
        waited.setOrganizerId(1L);

        doReturn(List.of(event())).when(repository).findAll();

        var result = service.findAll();

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(waited));
    }

    @DisplayName("Должен удалить событие")
    @Test
    void delete() {
        assertThatCode(() -> service.delete(1L)).doesNotThrowAnyException();
    }
}