package ru.otus.project.entities.enums;

import lombok.Getter;

/**
 * Тип события.
 */
@Getter
public enum EventType {
    PRESENTATION("Презентация"),
    TRAINING("Семинар"),
    EXHIBITION("Выставка"),
    CONFERENCE("Конференция"),
    CORPORATE("Корпоративное мероприятие"),
    FESTIVAL("Фестиваль"),
    CHARITY("Благотворительное мероприятие"),
    SPORTING("Спортивное мероприятие"),
    ONLINE("Онлайн мероприятие");

    /**
     * Описание типа.
     */
    private final String description;

    EventType(String description) {
        this.description = description;
    }
}
