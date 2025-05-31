package ru.otus.project.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.otus.project.entities.Organizer;

@Schema(description = "ДТО ответа на запросы по организаторам")
public record OrganizerResponseDTO(@Schema(description = "Идентификатор")
                                   Long id,
                                   @Schema(description = "Логин")
                                   String login) {

    /**
     * Создание ДТО на основании данных о организаторе.
     *
     * @param organizer организатор.
     * @return ДТО.
     */
    public static OrganizerResponseDTO fromOrganizer(Organizer organizer) {
        return new OrganizerResponseDTO(organizer.getId(), organizer.getLogin());
    }
}
