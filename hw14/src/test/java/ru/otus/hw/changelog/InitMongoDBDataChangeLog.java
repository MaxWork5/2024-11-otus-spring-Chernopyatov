package ru.otus.hw.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.entities.mongo.MongoAuthor;
import ru.otus.hw.entities.mongo.MongoBook;
import ru.otus.hw.entities.mongo.MongoComment;
import ru.otus.hw.entities.mongo.MongoGenre;
import ru.otus.hw.repositories.mongo.MongoAuthorRepository;
import ru.otus.hw.repositories.mongo.MongoBookRepository;
import ru.otus.hw.repositories.mongo.MongoCommentRepository;
import ru.otus.hw.repositories.mongo.MongoGenreRepository;

import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    @ChangeSet(order = "001", id = "dropDb", author = "mch", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "mch", runAlways = true)
    public void insertAuthors(MongoAuthorRepository authorRepository) {
        List<MongoAuthor> authors = List.of(new MongoAuthor("Author_1"),
                new MongoAuthor("Author_2"),
                new MongoAuthor("Author_3"));
        authorRepository.saveAll(authors);
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "mch", runAlways = true)
    public void insertGenres(MongoGenreRepository genreRepository) {
        List<MongoGenre> genres = List.of(new MongoGenre("Genre_1"),
                new MongoGenre("Genre_2"),
                new MongoGenre("Genre_3"),
                new MongoGenre("Genre_4"),
                new MongoGenre("Genre_5"),
                new MongoGenre("Genre_6"));
        genreRepository.saveAll(genres);
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "mch", runAlways = true)
    public void insertBooks(MongoBookRepository bookRepository,
                            MongoGenreRepository genreRepository,
                            MongoAuthorRepository authorRepository) {
        List<MongoBook> books = List.of(
                new MongoBook("BookTitle_1",
                        authorRepository.findByFullName("Author_1").orElse(null),
                        genreRepository.findAllByNameIn(Set.of("Genre_1", "Genre_2"))),
                new MongoBook("BookTitle_2",
                        authorRepository.findByFullName("Author_2").orElse(null),
                        genreRepository.findAllByNameIn(Set.of("Genre_3", "Genre_4"))),
                new MongoBook("BookTitle_3",
                        authorRepository.findByFullName("Author_3").orElse(null),
                        genreRepository.findAllByNameIn(Set.of("Genre_5", "Genre_6"))));
        bookRepository.saveAll(books);
    }

    @ChangeSet(order = "005", id = "insertComments", author = "mch", runAlways = true)
    public void insertComments(MongoCommentRepository commentRepository, MongoBookRepository bookRepository) {
        List<MongoComment> comments = List.of(
                new MongoComment("Wonderful. Wonderful piece of art. Worth your time.",
                        bookRepository.findByTitle("BookTitle_1").map(MongoBook::getId).orElse(null)),
                new MongoComment("A good piece for the evening",
                        bookRepository.findByTitle("BookTitle_2").map(MongoBook::getId).orElse(null)),
                new MongoComment("How can you read this?",
                        bookRepository.findByTitle("BookTitle_1").map(MongoBook::getId).orElse(null)));
        commentRepository.saveAll(comments);
    }
}
