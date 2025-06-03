package ru.otus.project.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.project.entities.Organizer;
import ru.otus.project.exceptions.OrganizerNotFoundException;
import ru.otus.project.repositories.OrganizerRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@DisplayName("Тест сервиса организаторов")
@ExtendWith(MockitoExtension.class)
class OrganizerServiceImplementationTest {

    @Mock
    private OrganizerRepository repository;

    @InjectMocks
    private OrganizerServiceImplementation service;

    private static Organizer organizer() {
        var organizer = new Organizer();
        organizer.setId(1L);
        organizer.setLogin("organizer");
        return organizer;
    }

    @DisplayName("Должен вернуть всех организаторов")
    @Test
    void findAll() {
        var waited = new Organizer();
        waited.setId(1L);
        waited.setLogin("organizer");

        doReturn(List.of(organizer())).when(repository).findAll();

        var result = service.findAll();

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(waited));
    }

    @DisplayName("Должен вернуть организатора по идентификатору")
    @Test
    void findById() {
        var waited = new Organizer();
        waited.setId(1L);
        waited.setLogin("organizer");

        doReturn(Optional.of(organizer())).when(repository).findById(1L);

        var result = service.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(waited);
    }

    @DisplayName("Должен отбросить ошибку, что организатор не найден при поиске организатора")
    @Test
    void findById_OrganizerNotFound() {
        doReturn(Optional.empty()).when(repository).findById(1L);

        assertThatThrownBy(() -> service.findById(1L)).isInstanceOf(OrganizerNotFoundException.class);
    }

    @DisplayName("Должен создать организатора")
    @Test
    void insert() {
        var waited = new Organizer();
        waited.setId(1L);
        waited.setLogin("organizer");

        doReturn(organizer()).when(repository).save(any());

        var result = service.insert("organizer");

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(waited);
    }

    @DisplayName("Должен изменить организатора")
    @Test
    void update() {
        var waited = new Organizer();
        waited.setId(1L);
        waited.setLogin("organizer");

        doReturn(true).when(repository).existsById(1L);
        doReturn(organizer()).when(repository).save(any());

        var result = service.update(1L, "organizer");

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(waited);
    }

    @DisplayName("Должен отбросить ошибку, что организатор не найден при изменении организатора")
    @Test
    void update_OrganizerNotFound() {
        doReturn(false).when(repository).existsById(1L);

        assertThatThrownBy(() -> service.update(1L, "organizer")).isInstanceOf(OrganizerNotFoundException.class);
    }

    @DisplayName("Должен удалить организатора")
    @Test
    void delete() {
        assertThatCode(() -> service.delete(1L)).doesNotThrowAnyException();
    }
}