package ru.otus.project.dto.requests.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ru.otus.project.dto.requests.ClientSendRequestDTO;

import java.util.List;

@Schema(description = "ДТО изменения пользователя")
public record MemberUpdateRequestDTO(@Schema(description = "Логин") @NotNull
                                     String login,
                                     @Schema(description = "Идентификаторы событий")
                                     List<Long> idEvents) implements ClientSendRequestDTO {
}
