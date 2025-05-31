package ru.otus.project.dto.request.client;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "ДТО для создания пользователя")
public record ClientInsertRequestDTO(@Schema(description = "Логин") String login) {
}
