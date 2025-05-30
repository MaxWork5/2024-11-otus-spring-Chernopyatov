package ru.otus.project.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.otus.project.dto.request.review.ReviewInsertRequestDTO;
import ru.otus.project.dto.request.review.ReviewUpdateRequestDTO;
import ru.otus.project.dto.response.CollectionWrapperDTO;
import ru.otus.project.dto.response.ReviewResponseDTO;
import ru.otus.project.services.ReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService service;

    @Operation(summary = "Запрос на создание отзыва")
    @PostMapping("/api/reviews")
    public ResponseEntity<ReviewResponseDTO> addReview(@RequestBody ReviewInsertRequestDTO dto) {
        var response = ReviewResponseDTO.fromReview(service.insert(dto.comment(), dto.eventId()));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Запрос на получение отзывов по мероприятиям организатора")
    @GetMapping("/api/events/reviews")
    public ResponseEntity<CollectionWrapperDTO> getReviews(@RequestParam List<Long> eventId) {
        var response = service.findReviews(eventId).stream()
                .map(ReviewResponseDTO::fromReview)
                .toList();

        return ResponseEntity.ok(new CollectionWrapperDTO(response));
    }

    @Operation(summary = "Запрос на получение всех отзывов")
    @GetMapping("/api/reviews")
    public ResponseEntity<CollectionWrapperDTO> getReviews() {
        var response = service.findAll().stream()
                .map(ReviewResponseDTO::fromReview)
                .toList();

        return ResponseEntity.ok(new CollectionWrapperDTO(response));
    }

    @Operation(summary = "Запрос на получение отзыва")
    @GetMapping("/api/reviews/{id}")
    public ResponseEntity<ReviewResponseDTO> getReview(@PathVariable Long id) {
        var response = ReviewResponseDTO.fromReview(service.findById(id));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Запрос на изменение отзыва")
    @PutMapping("/api/reviews/{id}")
    public ResponseEntity<ReviewResponseDTO> updateReview(@PathVariable Long id,
                                                          @RequestBody ReviewUpdateRequestDTO dto) {
        var response = ReviewResponseDTO.fromReview(service.update(id, dto.comment()));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Запрос на удаления отзыва")
    @DeleteMapping("/api/reviews/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.ok().build();
    }
}
