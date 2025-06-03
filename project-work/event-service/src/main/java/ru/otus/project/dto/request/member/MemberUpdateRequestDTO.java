package ru.otus.project.dto.request.member;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "ДТО изменения пользователя")
public record MemberUpdateRequestDTO(@Schema(description = "Логин")
                                     String login,
                                     @Schema(description = "Идентификаторы событий")
                                     List<Long> idEvents) {
}
