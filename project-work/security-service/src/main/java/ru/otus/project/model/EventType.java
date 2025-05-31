package ru.otus.project.model;

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

    private final String description;

    EventType(String description) {
        this.description = description;
    }
}
