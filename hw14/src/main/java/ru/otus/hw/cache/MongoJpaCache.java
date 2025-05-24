package ru.otus.hw.cache;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.otus.hw.entities.jpa.JpaAuthor;
import ru.otus.hw.entities.jpa.JpaBook;
import ru.otus.hw.entities.jpa.JpaGenre;

import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class MongoJpaCache {
    private final Map<String, JpaAuthor> authorCache = new HashMap<>();

    private final Map<String, JpaGenre> genreCache = new HashMap<>();

    private final Map<String, JpaBook> bookCache = new HashMap<>();

    public void addAuthor(String key, JpaAuthor author) {
        authorCache.put(key, author);
    }

    public void addGenre(String key, JpaGenre genre) {
        genreCache.put(key, genre);
    }

    public void addBook(String key, JpaBook book) {
        bookCache.put(key, book);
    }

    public void clear() {
        authorCache.clear();
        genreCache.clear();
        bookCache.clear();
    }
}
