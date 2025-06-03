package ru.otus.project.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.project.entities.Event;
import ru.otus.project.entities.Review;
import ru.otus.project.exceptions.EventNotFoundException;
import ru.otus.project.exceptions.ReviewNotFoundException;
import ru.otus.project.repositories.EventRepository;
import ru.otus.project.repositories.ReviewRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@DisplayName("Тест сервиса отзывов")
@ExtendWith(MockitoExtension.class)
class ReviewServiceImplementationTest {
    @Mock
    ReviewRepository repository;

    @Mock
    EventRepository eventRepository;

    @InjectMocks
    ReviewServiceImplementation service;

    private static Review review() {
        var review = new Review();
        review.setId(1L);
        review.setComment("comment");
        review.setEventId(1L);
        return review;
    }

    @DisplayName("Должен создать отзыв")
    @Test
    void insert() {
        var waited = new Review();
        waited.setId(1L);
        waited.setComment("comment");
        waited.setEventId(1L);

        doReturn(Optional.of(new Event())).when(eventRepository).findById(1L);
        doReturn(review()).when(repository).save(any());

        var result = service.insert("comment", 1L);

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(waited);
    }

    @DisplayName("Должен отбросить ошибку, что организатор не найден при создании отзыва")
    @Test
    void insert_OrganizerNotFound() {
        doReturn(Optional.empty()).when(eventRepository).findById(1L);

        assertThatCode(() -> service.insert("comment", 1L)).isInstanceOf(EventNotFoundException.class);
    }

    @DisplayName("Должен найти список отзывов по идентификаторам событий")
    @Test
    void findReviews() {
        var waited = new Review();
        waited.setId(1L);
        waited.setComment("comment");
        waited.setEventId(1L);

        doReturn(List.of(review())).when(repository).findAllByEventIdInOrderByEventId(List.of(1L));

        var result = service.findReviews(List.of(1L));

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(waited));
    }

    @DisplayName("Должен вернуть все отзывы")
    @Test
    void findAll() {
        var waited = new Review();
        waited.setId(1L);
        waited.setComment("comment");
        waited.setEventId(1L);

        doReturn(List.of(review())).when(repository).findAll();

        var result = service.findAll();

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(waited));
    }

    @DisplayName("Должен вернуть отзыв по идентификатору")
    @Test
    void findById() {
        var waited = new Review();
        waited.setId(1L);
        waited.setComment("comment");
        waited.setEventId(1L);

        doReturn(Optional.of(review())).when(repository).findById(1L);

        var result = service.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(waited);
    }

    @DisplayName("Должен отбросить ошибку, что отзыв не найден при поиске отзыва")
    @Test
    void findById_ReviewNotFound() {
        doReturn(Optional.empty()).when(repository).findById(1L);

        assertThatThrownBy(() -> service.findById(1L)).isInstanceOf(ReviewNotFoundException.class);
    }

    @DisplayName("Должен изменить отзыв")
    @Test
    void update() {
        var waited = new Review();
        waited.setId(1L);
        waited.setComment("newcomment");
        waited.setEventId(1L);
        var returned = new Review();
        returned.setId(1L);
        returned.setComment("newcomment");
        returned.setEventId(1L);

        doReturn(Optional.of(review())).when(repository).findById(1L);
        doReturn(returned).when(repository).save(any());

        var result = service.update(1L,"newcomment");

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(returned);
    }

    @DisplayName("Должен отбросить ошибку, что отзыв не найден при изменении отзыва")
    @Test
    void update_ReviewNotFound() {
        doReturn(Optional.empty()).when(repository).findById(1L);

        assertThatThrownBy(() -> service.update(1L,"newcomment")).isInstanceOf(ReviewNotFoundException.class);
    }

    @DisplayName("Должен удалить отзыв")
    @Test
    void delete() {
        assertThatCode(() -> service.delete(1L)).doesNotThrowAnyException();
    }
}