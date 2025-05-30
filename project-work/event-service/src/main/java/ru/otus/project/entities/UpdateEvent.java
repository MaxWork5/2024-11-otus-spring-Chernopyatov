package ru.otus.project.entities;

import ru.otus.project.dto.request.event.EventUpdateRequestDTO;
import ru.otus.project.entities.enums.EventType;

import java.time.LocalDate;

/**
 * Событие обновления.
 *
 * @param id          идентификатор события.
 * @param name        наименование.
 * @param description описание
 * @param date        дата.
 * @param type        тип.
 * @param organizerId идентификатор организатора.
 */
public record UpdateEvent(Long id, String name, String description, LocalDate date, EventType type, Long organizerId) {

    /**
     * Создание события обновления на основании DTO.
     *
     * @param id  идентификатор обновляемого события.
     * @param dto ДТО.
     * @return событие обновления.
     */
    public static UpdateEvent fromDto(Long id, EventUpdateRequestDTO dto) {
        return new UpdateEvent(id,
                dto.name(),
                dto.description(),
                dto.date(),
                dto.type(),
                dto.organizerId());
    }
}
