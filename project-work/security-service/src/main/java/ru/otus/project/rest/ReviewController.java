package ru.otus.project.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.project.client.EventServiceClient;
import ru.otus.project.dto.responses.ClientSendResponseDTO;
import ru.otus.project.dto.requests.review.ReviewInsertRequestDTO;
import ru.otus.project.dto.requests.review.ReviewUpdateRequestDTO;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final EventServiceClient client;

    @Operation(summary = "Запрос на создание отзыва")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SCOPE_REVIEW_INSERT')")
    @PostMapping("/api/reviews")
    public ClientSendResponseDTO addReview(@Validated @RequestBody ReviewInsertRequestDTO dto) {
        return client.sendPostRequest("/api/reviews", dto);
    }

    @Operation(summary = "Запрос на получение отзывов по мероприятиям организатора")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_REVIEW_GET')")
    @GetMapping("/api/events/reviews")
    public ClientSendResponseDTO getReviews(@RequestParam List<Long> ids) {
        StringBuilder path = new StringBuilder("/api/events/reviews?");
        var first = true;
        for (Long id : ids) {
            if (!first) {
                path.append("&");
            }
            path.append("eventId=").append(id);
            first = false;
        }
        return client.sendGetRequest(path.toString(), null);
    }

    @Operation(summary = "Запрос на получение всех отзывов")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_REVIEW_ALL')")
    @GetMapping("/api/reviews/all")
    public ClientSendResponseDTO getAllReviews() {
        return client.sendGetRequest("/api/reviews", null);
    }

    @Operation(summary = "Запрос на получение отзыва")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_REVIEW_ALL')")
    @GetMapping("/api/reviews/{idReview}")
    public ClientSendResponseDTO getReview(@PathVariable Long idReview) {
        return client.sendGetRequest("/api/reviews/{id}", idReview.toString());
    }

    @Operation(summary = "Запрос на изменение отзыва")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_REVIEW_UPDATE')")
    @PutMapping("/api/reviews/{id}")
    public ClientSendResponseDTO updateReview(@PathVariable Long id,
                                              @Validated @RequestBody ReviewUpdateRequestDTO dto) {
        return client.sendPutRequest("/api/reviews/{id}", id, dto);
    }

    @Operation(summary = "Запрос на удаления отзыва")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_REVIEW_DELETE')")
    @DeleteMapping("/api/reviews/{idReview}")
    public void deleteReview(@PathVariable Long idReview) {
        client.sendDeleteRequest("/api/reviews/{id}", idReview.toString());
    }
}
