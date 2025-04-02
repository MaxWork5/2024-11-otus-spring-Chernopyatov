package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.entities.jpa.JpaGenre;

@Component
public class GenreConverter {
    public String genreToString(JpaGenre genre) {
        return "Id: %d, Name: %s".formatted(genre.getId(), genre.getName());
    }
}