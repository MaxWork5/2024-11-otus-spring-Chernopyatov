package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.entities.jpa.JpaAuthor;

@Component
public class AuthorConverter {
    public String authorToString(JpaAuthor author) {
        return "Id: %d, FullName: %s".formatted(author.getId(), author.getFullName());
    }
}