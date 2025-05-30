package ru.otus.project.dto.requests.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ru.otus.project.dto.requests.ClientSendRequestDTO;

@Schema(description = "ДТО записи пользователя на событие")
public record EntryRequestDTO(@NotNull @Schema(description = "Идентификатор события") Long idEntry)
        implements ClientSendRequestDTO {
}
