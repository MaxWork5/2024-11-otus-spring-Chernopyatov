package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.domain.Comment;

@Data
@AllArgsConstructor
public class CommentDto {
    private long id;

    private String description;

    private long bookId;

    public static CommentDto fromDomainObject(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getDescription(),
                comment.getBookId());
    }
}