package ru.otus.project.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.otus.project.entities.enums.EventType;

import java.time.LocalDate;
import java.util.List;

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

    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 5)
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "entries",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    private List<Member> members;

    public Event(String name, String description, LocalDate date, EventType type, Long organizerId) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.type = type;
        this.organizerId = organizerId;
    }
}
