package ru.otus.project.dto.request.member;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "ДТО записи пользователя на событие")
public record EntryRequestDTO(@Schema(description = "Идентификатор события") Long idEntry) {
}
