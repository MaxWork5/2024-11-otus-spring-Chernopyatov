package ru.otus.project.dto.request.event;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.otus.project.entities.enums.EventType;

import java.time.LocalDate;

@Schema(description = "ДТО поиска событий по условиям")
public record EventQueryDTO(@Schema(description = "Дата, с которой начинается отображение событий")
                            LocalDate startDate,
                            @Schema(description = "Дата, с которой заканчивается отображение событий")
                            LocalDate endDate,
                            @Schema(description = "Тип события")
                            EventType type) {
}
