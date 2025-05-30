package ru.otus.project.dto.requests.event;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ru.otus.project.dto.requests.ClientSendRequestDTO;
import ru.otus.project.model.EventType;

import java.time.LocalDate;

@Schema(description = "ДТО создания события")
public record EventInsertRequestDTO(@Schema(description = "Название события") @NotNull
                                    String name,
                                    @Schema(description = "Описание")
                                    String description,
                                    @Schema(description = "Дата") @NotNull
                                    LocalDate date,
                                    @Schema(description = "Тип события") @NotNull
                                    EventType type,
                                    @Schema(description = "Идентификатор организатора") @NotNull
                                    Long organizerId) implements ClientSendRequestDTO {
}
