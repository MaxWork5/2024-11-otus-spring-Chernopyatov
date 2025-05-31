package ru.otus.project.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.project.entities.enums.EventType;

import java.time.LocalDate;

/**
 * Событие.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    /**
     * Идентификатор записи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Наименование события.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Описание события.
     */
    @Column(name = "description")
    private String description;

    /**
     * Дата события.
     */
    @Column(name = "date_of_event", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate date;

    /**
     * Тип события.
     */
    @Column(name = "type_of_event", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType type;

    /**
     * Идентификатор организатора события.
     */
    @Column(name = "organizer_id", nullable = false)
    private Long organizerId;

    public Event(String name, String description, LocalDate date, EventType type, Long organizerId) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.type = type;
        this.organizerId = organizerId;
    }
}
