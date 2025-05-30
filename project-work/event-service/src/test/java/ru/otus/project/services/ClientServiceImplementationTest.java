package ru.otus.project.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.project.entities.Client;
import ru.otus.project.entities.Event;
import ru.otus.project.entities.enums.EventType;
import ru.otus.project.exceptions.ClientNotFoundException;
import ru.otus.project.exceptions.EventNotFoundException;
import ru.otus.project.repositories.ClientRepository;
import ru.otus.project.repositories.EventRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@DisplayName("Тест сервиса пользователей")
@ExtendWith(MockitoExtension.class)
class ClientServiceImplementationTest {
    @Mock
    ClientRepository repository;

    @Mock
    EventRepository eventRepository;

    @InjectMocks
    ClientServiceImplementation service;

    private static Client client() {
        var client = new Client();
        client.setId(1L);
        client.setLogin("testlogin");
        client.setEvents(new ArrayList<>(List.of()));
        return client;
    }

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

    @DisplayName("Должен создать пользователя")
    @Test
    void insert() {
        var waited = new Client();
        waited.setId(1L);
        waited.setLogin("testlogin");
        waited.setEvents(List.of());

        doReturn(client()).when(repository).save(any());

        var result = service.insert("testlogin");

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(waited);
    }

    @DisplayName("Должен добавить событие пользователю")
    @Test
    void signUp() {
        var waited = new Client();
        waited.setId(1L);
        waited.setLogin("testlogin");
        waited.setEvents(List.of(event()));
        var returned = new Client();
        returned.setId(1L);
        returned.setLogin("testlogin");
        returned.setEvents(List.of(event()));

        doReturn(Optional.of(event())).when(eventRepository).findById(1L);
        doReturn(Optional.of(client())).when(repository).findById(1L);
        doReturn(returned).when(repository).save(any());

        var result = service.signUp(1L, 1L);

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(waited);

    }

    @DisplayName("Должен выбросить ошибку, что пользователь не найден при добавлении события пользователю")
    @Test
    void signUp_ClientNotFound() {
        doReturn(Optional.empty()).when(repository).findById(1L);

        assertThatThrownBy(() -> service.signUp(1L, 1L)).isInstanceOf(ClientNotFoundException.class);
    }

    @DisplayName("Должен выбросить ошибку, что событие не найдено при добавлении события пользователю")
    @Test
    void signUp_EventNotFound() {
        doReturn(Optional.of(client())).when(repository).findById(1L);
        doReturn(Optional.empty()).when(eventRepository).findById(1L);

        assertThatThrownBy(() -> service.signUp(1L, 1L)).isInstanceOf(EventNotFoundException.class);
    }

    @DisplayName("Должен вернуть список событий, в которых принимает участие пользователь")
    @Test
    void lookOnEvents() {
        var newClient = client();
        newClient.setEvents(List.of(event()));

        doReturn(Optional.of(newClient)).when(repository).findById(1L);

        var result = service.lookOnEvents(1L);

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(event()));
    }

    @DisplayName("Должен выбросить ошибку, что пользователь не найден при поиске событий пользователя")
    @Test
    void lookOnEvents_ClientNotFound() {
        doReturn(Optional.empty()).when(repository).findById(1L);

        assertThatThrownBy(() -> service.lookOnEvents(1L)).isInstanceOf(ClientNotFoundException.class);
    }

    @DisplayName("Должен вернуть пользователей, которые участвуют в событии")
    @Test
    void findMembersOfEvent() {
        var newClient = client();
        newClient.setEvents(List.of(event()));

        doReturn(List.of(newClient)).when(repository).findAllByEventId(1L);

        var result = service.findMembersOfEvent(1L);

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(newClient));
    }

    @DisplayName("Должен удалить пользователя")
    @Test
    void deleteClient() {
        assertThatCode(() -> service.deleteClient(1L)).doesNotThrowAnyException();
    }

    @DisplayName("Должен изменить пользователя")
    @Test
    void updateClient() {
        var waited = new Client();
        waited.setId(1L);
        waited.setLogin("newlogin");
        waited.setEvents(List.of(event()));
        var returned = new Client();
        returned.setId(1L);
        returned.setLogin("newlogin");
        returned.setEvents(List.of(event()));

        doReturn(List.of(event())).when(eventRepository).findAllById(List.of(1L));
        doReturn(Optional.of(client())).when(repository).findById(1L);
        doReturn(returned).when(repository).save(any());

        var result = service.updateClient(1L,"newlogin", List.of(1L));

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(waited);
    }

    @DisplayName("Должен выбросить ошибку, что пользователь не найден при изменении пользователя")
    @Test
    void updateClient_clientNotFound() {
        doReturn(Optional.empty()).when(repository).findById(1L);

        assertThatThrownBy(() -> service.updateClient(1L,"newlogin", List.of(1L)))
                .isInstanceOf(ClientNotFoundException.class);
    }

    @DisplayName("Должен вернуть всех пользователей")
    @Test
    void findAll() {
        var waited = new Client();
        waited.setId(1L);
        waited.setLogin("testlogin");
        waited.setEvents(List.of(event()));
        var newClient = client();
        newClient.setEvents(List.of(event()));

        doReturn(List.of(newClient)).when(repository).findAll();

        var result = service.findAll();

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(waited));
    }

    @DisplayName("Должен вернуть пользователя по идентификатору")
    @Test
    void findById() {
        var waited = new Client();
        waited.setId(1L);
        waited.setLogin("testlogin");
        waited.setEvents(List.of(event()));
        var newClient = client();
        newClient.setEvents(List.of(event()));

        doReturn(Optional.of(newClient)).when(repository).findById(1L);

        var result = service.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(waited);
    }

    @DisplayName("Должен выбросить ошибку, что пользователь не найден при поиске пользователя")
    @Test
    void findById_ClientNotFound() {
        doReturn(Optional.empty()).when(repository).findById(1L);

        assertThatThrownBy(() -> service.findById(1L)).isInstanceOf(ClientNotFoundException.class);
    }
}