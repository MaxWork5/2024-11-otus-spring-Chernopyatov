package ru.otus.project.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.otus.project.entities.Member;

import java.util.List;

@Schema(description = "ДТО ответа на запросы по пользователю")
public record MemberResponseDTO(@Schema(description = "Идентификатор")
                                Long id,
                                @Schema(description = "Логин")
                                String login,
                                @Schema(description = "Список событий")
                                List<EventResponseDTO> events) {

    /**
     * Создание ДТО на основании данных о клиенте.
     *
     * @param member клиент.
     * @return ДТО.
     */
    public static MemberResponseDTO fromClient(Member member) {
        return new MemberResponseDTO(member.getId(),
                member.getLogin(),
                member.getEvents() == null || member.getEvents().isEmpty() ?
                        null :
                        member.getEvents().stream()
                                .map(EventResponseDTO::fromEvent)
                                .toList());
    }
}
