package ru.otus.hw.configuration.writer;

import lombok.AllArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import ru.otus.hw.cache.MongoJpaCache;
import ru.otus.hw.entities.jpa.JpaAuthor;
import ru.otus.hw.entities.mongo.MongoAuthor;
import ru.otus.hw.repositories.jpa.JpaAuthorRepository;

@AllArgsConstructor
public class JpaAuthorWriter implements ItemWriter<MongoAuthor> {

    private MongoJpaCache cache;

    private JpaAuthorRepository repository;

    @Override
    public void write(Chunk<? extends MongoAuthor> chunk) {
        for (MongoAuthor mongoAuthor : chunk) {
            var jpaAuthor = new JpaAuthor();
            jpaAuthor.setFullName(mongoAuthor.getFullName());
            var savedAuthor = repository.save(jpaAuthor);
            cache.addAuthor(mongoAuthor.getId(), savedAuthor);
        }
    }
}
