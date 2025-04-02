package ru.otus.hw.entities.mongo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@Document(collection = "books")
public class MongoBook {
    @Id
    private String id;

    @Field(name = "title")
    private String title;

    @DBRef
    private MongoAuthor author;

    @DBRef
    private List<MongoGenre> genres;

    public MongoBook(String title, MongoAuthor author, List<MongoGenre> genres) {
        this.title = title;
        this.author = author;
        this.genres = genres;
    }
}
