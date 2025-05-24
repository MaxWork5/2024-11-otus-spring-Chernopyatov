package ru.otus.hw.configuration.writer;

import lombok.AllArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import ru.otus.hw.cache.MongoJpaCache;
import ru.otus.hw.entities.jpa.JpaGenre;
import ru.otus.hw.entities.mongo.MongoGenre;
import ru.otus.hw.repositories.jpa.JpaGenreRepository;

@AllArgsConstructor
public class JpaGenreWriter implements ItemWriter<MongoGenre> {

    private MongoJpaCache cache;

    private JpaGenreRepository repository;

    @Override
    public void write(Chunk<? extends MongoGenre> chunk) {
        for (MongoGenre mongoGenre : chunk) {
            var jpaGenre = new JpaGenre();
            jpaGenre.setName(mongoGenre.getName());
            var savedAuthor = repository.save(jpaGenre);
            cache.addGenre(mongoGenre.getId(), savedAuthor);
        }
    }
}
