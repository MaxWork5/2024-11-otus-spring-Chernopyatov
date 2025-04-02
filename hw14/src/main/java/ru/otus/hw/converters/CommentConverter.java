package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.entities.jpa.JpaComment;

@RequiredArgsConstructor
@Component
public class CommentConverter {
    public String commentToString(JpaComment comment) {
        return "Id: %d, description: %s, bookId: %d".formatted(
                comment.getId(),
                comment.getDescription(),
                comment.getBookId());
    }
}