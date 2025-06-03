package ru.otus.project.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.otus.project.entities.Review;

@Schema(description = "ДТО ответа на запросы по отзывам")
public record ReviewResponseDTO(@Schema(description = "Идентификатор")
                                Long id,
                                @Schema(description = "Комментарий")
                                String comment,
                                @Schema(description = "Идентификатор события")
                                Long eventId) {

    /**
     * Создание ДТО на основании данных об отзыве.
     *
     * @param review отзыв.
     * @return ДТО.
     */
    public static ReviewResponseDTO fromReview(Review review) {
        return new ReviewResponseDTO(review.getId(), review.getComment(), review.getEventId());
    }
}
