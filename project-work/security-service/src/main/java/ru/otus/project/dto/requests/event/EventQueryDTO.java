package ru.otus.project.dto.requests.event;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ru.otus.project.dto.requests.ClientSendRequestDTO;
import ru.otus.project.model.EventType;

import java.time.LocalDate;

@Schema(description = "ДТО поиска событий по условиям")
public record EventQueryDTO(@Schema(description = "Дата, с которой начинается отображение событий") @NotNull
                            LocalDate startDate,
                            @Schema(description = "Дата, с которой заканчивается отображение событий") @NotNull
                            LocalDate endDate,
                            @Schema(description = "Тип события") @NotNull
                            EventType type) implements ClientSendRequestDTO {
}
