package ru.otus.project.dto.requests.review;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ru.otus.project.dto.requests.ClientSendRequestDTO;

@Schema(description = "ДТО создания отзыва")
public record ReviewInsertRequestDTO(@Schema(description = "Комментарий")
                                     String comment,
                                     @Schema(description = "Идентификатор события") @NotNull
                                     Long eventId) implements ClientSendRequestDTO {
}
