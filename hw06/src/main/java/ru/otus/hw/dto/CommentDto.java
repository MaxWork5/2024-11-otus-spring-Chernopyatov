package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Comment;

@Data
@AllArgsConstructor
public class CommentDto {
    private long id;

    private String description;

    private BookDto book;

    public static CommentDto fromDomainObject(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getDescription(),
                BookDto.fromDomainObject(comment.getBook()));
    }
}
