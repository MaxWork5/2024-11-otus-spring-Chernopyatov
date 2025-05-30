package ru.otus.project.dto.requests.review;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.otus.project.dto.requests.ClientSendRequestDTO;

@Schema(description = "ДТО изменения отзыва")
public record ReviewUpdateRequestDTO(@Schema(description = "Комментарий")
                                     String comment) implements ClientSendRequestDTO {
}
