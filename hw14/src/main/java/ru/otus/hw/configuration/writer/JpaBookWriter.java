package ru.otus.hw.configuration.writer;

import lombok.AllArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import ru.otus.hw.cache.MongoJpaCache;
import ru.otus.hw.entities.jpa.JpaBook;
import ru.otus.hw.entities.mongo.MongoBook;
import ru.otus.hw.repositories.jpa.JpaBookRepository;

@AllArgsConstructor
public class JpaBookWriter implements ItemWriter<MongoBook> {
    private MongoJpaCache cache;

    private JpaBookRepository repository;

    @Override
    public void write(Chunk<? extends MongoBook> chunk) {
        for (MongoBook mongoBook : chunk) {
            var jpaBook = new JpaBook();
            jpaBook.setTitle(mongoBook.getTitle());
            var authors = cache.getAuthorCache();
            jpaBook.setAuthor(authors.get(mongoBook.getAuthor().getId()));
            var genres = cache.getGenreCache();
            var listOfGenres = mongoBook.getGenres().stream()
                    .map(genre -> genres.get(genre.getId()))
                    .toList();
            jpaBook.setGenres(listOfGenres);
            var savedBook = repository.save(jpaBook);
            cache.addBook(mongoBook.getId(), savedBook);
        }
    }
}
