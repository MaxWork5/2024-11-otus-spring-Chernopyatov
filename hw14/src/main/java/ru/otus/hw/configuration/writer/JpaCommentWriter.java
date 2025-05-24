package ru.otus.hw.configuration.writer;

import lombok.AllArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import ru.otus.hw.cache.MongoJpaCache;
import ru.otus.hw.entities.jpa.JpaComment;
import ru.otus.hw.entities.mongo.MongoComment;
import ru.otus.hw.repositories.jpa.JpaCommentRepository;

@AllArgsConstructor
public class JpaCommentWriter implements ItemWriter<MongoComment> {

    private MongoJpaCache cache;

    private JpaCommentRepository repository;

    @Override
    public void write(Chunk<? extends MongoComment> chunk) {
        for (MongoComment mongoComment : chunk) {
            var jpaComment = new JpaComment();
            jpaComment.setDescription(mongoComment.getDescription());
            var books = cache.getBookCache();
            jpaComment.setBookId(books.get(mongoComment.getBookId()).getId());
            repository.save(jpaComment);
        }
    }
}
