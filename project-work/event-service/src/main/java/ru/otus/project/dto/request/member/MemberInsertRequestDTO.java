package ru.otus.project.dto.request.member;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "ДТО для создания пользователя")
public record MemberInsertRequestDTO(@Schema(description = "Логин") String login) {
}
