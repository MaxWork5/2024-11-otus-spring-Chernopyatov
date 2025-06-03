package ru.otus.project.dto.requests.organizer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ru.otus.project.dto.requests.ClientSendRequestDTO;

@Schema(description = "ДТО изменения организатора")
public record OrganizerUpdateRequestDTO(@Schema(description = "Логин") @NotNull
                                        String login) implements ClientSendRequestDTO {
}
