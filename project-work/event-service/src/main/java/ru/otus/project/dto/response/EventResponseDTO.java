package ru.otus.project.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.otus.project.entities.Event;

@Schema(description = "ДТО ответа на запросы по событиям")
public record EventResponseDTO(@Schema(description = "Идентификатор")
                               Long id,
                               @Schema(description = "Имя события")
                               String name,
                               @Schema(description = "Описание")
                               String description,
                               @Schema(description = "Дата")
                               String date,
                               @Schema(description = "Тип события")
                               String type) {

    /**
     * Создание ДТО на основании данных о событии.
     *
     * @param event событие.
     * @return ДТО.
     */
    public static EventResponseDTO fromEvent(Event event) {
        return new EventResponseDTO(event.getId(),
                event.getName(),
                event.getDescription(),
                event.getDate().toString(),
                event.getType().getDescription());
    }
}
