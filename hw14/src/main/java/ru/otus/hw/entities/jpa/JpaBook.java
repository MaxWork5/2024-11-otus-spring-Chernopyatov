package ru.otus.hw.entities.jpa;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
@NamedEntityGraph(name = "book-author-entity-graph", attributeNodes = {@NamedAttributeNode("author")})
public class JpaBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @ManyToOne(targetEntity = JpaAuthor.class,
            cascade = CascadeType.MERGE,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private JpaAuthor author;

    @ManyToMany(targetEntity = JpaGenre.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "books_genres",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<JpaGenre> genres;
}