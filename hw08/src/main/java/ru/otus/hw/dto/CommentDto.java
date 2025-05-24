package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Comment;

@Data
@AllArgsConstructor
public class CommentDto {
    private String id;

    private String description;

    private String bookId;

    public static CommentDto fromDomainObject(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getDescription(),
                comment.getBookId());
    }
}
