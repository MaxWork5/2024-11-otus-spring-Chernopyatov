package ru.otus.project.dto.request.event;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.otus.project.entities.enums.EventType;

import java.time.LocalDate;

@Schema(description = "ДТО изменения события")
public record EventUpdateRequestDTO(@Schema(description = "Название события")
                                    String name,
                                    @Schema(description = "Описание")
                                    String description,
                                    @Schema(description = "Дата")
                                    LocalDate date,
                                    @Schema(description = "Тип события")
                                    EventType type,
                                    @Schema(description = "Идентификатор организатора")
                                    Long organizerId) {
}
