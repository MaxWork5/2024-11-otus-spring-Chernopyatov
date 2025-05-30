package ru.otus.project.dto.request.review;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "ДТО изменения отзыва")
public record ReviewUpdateRequestDTO(@Schema(description = "Комментарий") String comment) {
}
