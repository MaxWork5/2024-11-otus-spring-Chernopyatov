package ru.otus.hw.entities.mongo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "genres")
public class MongoGenre {
    @Id
    private String id;

    @Field(name = "name")
    private String name;

    public MongoGenre(String name) {
        this.name = name;
    }
}
