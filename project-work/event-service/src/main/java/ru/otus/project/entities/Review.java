package ru.otus.project.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Отзыв.
 */
@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "reviews")
public class Review {
    /**
     * Идентификатор записи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Комментарий.
     */
    @Column(name = "comment")
    private String comment;

    /**
     * Идентификатор события.
     */
    @Column(name = "event_id")
    private Long eventId;

    public Review(String comment, long eventId) {
        this.comment = comment;
        this.eventId = eventId;
    }
}
