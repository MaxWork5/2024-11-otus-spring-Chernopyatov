package ru.otus.project.dto.request.organizer;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "ДТО создания организатора")
public record OrganizerInsertRequestDTO(@Schema(description = "Логин") String login) {
}
