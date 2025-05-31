package ru.otus.project.entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

/**
 * Клиент.
 */
@Getter
@Setter
@Entity
@Table(name = "clients")
public class Client {
    /**
     * Идентификатор записи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Логин клиента.
     */
    @Column(name = "login", nullable = false, unique = true)
    private String login;

    /**
     * Список событий клиента.
     */
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 5)
    @ManyToMany(targetEntity = Event.class, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "entries",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> events;
}
