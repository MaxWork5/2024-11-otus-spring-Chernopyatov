package ru.otus.hw.entities.mongo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "authors")
public class MongoAuthor {
    @Id
    private String id;

    @Field(name = "full_name")
    private String fullName;

    public MongoAuthor(String fullName) {
        this.fullName = fullName;
    }
}