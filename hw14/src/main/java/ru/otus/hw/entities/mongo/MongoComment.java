package ru.otus.hw.entities.mongo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "comments")
public class MongoComment {
    @Id
    private String id;

    @Field(name = "description")
    private String description;

    @Field(name = "book_id")
    private String bookId;

    public MongoComment(String description, String bookId) {
        this.description = description;
        this.bookId = bookId;
    }
}
