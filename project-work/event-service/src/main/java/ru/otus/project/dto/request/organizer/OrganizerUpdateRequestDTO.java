package ru.otus.project.dto.request.organizer;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "ДТО изменения организатора")
public record OrganizerUpdateRequestDTO(@Schema(description = "Логин") String login) {
}
