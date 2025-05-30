package ru.otus.project.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

/**
 * Организатор.
 */
@Getter
@Setter
@Entity
@Table(name = "organizers")
public class Organizer {
    /**
     * Идентификатор записи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Логин организатора.
     */
    @Column(name = "login", nullable = false, unique = true)
    private String login;
}
