package ru.otus.project.dto.request.review;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "ДТО создания отзыва")
public record ReviewInsertRequestDTO(@Schema(description = "Комментарий")
                                     String comment,
                                     @Schema(description = "Идентификатор события")
                                     Long eventId) {
}
