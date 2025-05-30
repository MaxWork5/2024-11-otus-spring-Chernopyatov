package ru.otus.project.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.otus.project.entities.Client;

import java.util.List;

@Schema(description = "ДТО ответа на запросы по пользователю")
public record ClientResponseDTO(@Schema(description = "Идентификатор")
                                Long id,
                                @Schema(description = "Логин")
                                String login,
                                @Schema(description = "Список событий")
                                List<EventResponseDTO> events) {

    /**
     * Создание ДТО на основании данных о клиенте.
     *
     * @param client клиент.
     * @return ДТО.
     */
    public static ClientResponseDTO fromClient(Client client) {
        return new ClientResponseDTO(client.getId(),
                client.getLogin(),
                client.getEvents() == null || client.getEvents().isEmpty() ?
                        null :
                        client.getEvents().stream()
                                .map(EventResponseDTO::fromEvent)
                                .toList());
    }
}
