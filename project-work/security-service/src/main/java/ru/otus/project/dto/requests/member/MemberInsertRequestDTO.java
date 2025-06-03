package ru.otus.project.dto.requests.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ru.otus.project.dto.requests.ClientSendRequestDTO;

@Schema(description = "ДТО для создания пользователя")
public record MemberInsertRequestDTO(@Schema(description = "Логин") @NotNull String login)
        implements ClientSendRequestDTO {
}
