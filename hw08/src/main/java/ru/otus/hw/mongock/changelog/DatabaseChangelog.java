package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "mch", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "mch", runAlways = true)
    public void insertAuthors(AuthorRepository authorRepository) {
        List<Author> authors = List.of(new Author("Author_1"),
                new Author("Author_2"),
                new Author("Author_3"));
        authorRepository.saveAll(authors);
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "mch", runAlways = true)
    public void insertGenres(GenreRepository genreRepository) {
        List<Genre> genres = List.of(new Genre("Genre_1"),
                new Genre("Genre_2"),
                new Genre("Genre_3"),
                new Genre("Genre_4"),
                new Genre("Genre_5"),
                new Genre("Genre_6"));
        genreRepository.saveAll(genres);
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "mch", runAlways = true)
    public void insertBooks(BookRepository bookRepository,
                            GenreRepository genreRepository,
                            AuthorRepository authorRepository) {
        List<Book> books = List.of(
                new Book("BookTitle_1",
                        authorRepository.findByFullName("Author_1").orElse(null),
                        genreRepository.findAllByNameIn(Set.of("Genre_1", "Genre_2"))),
                new Book("BookTitle_2",
                        authorRepository.findByFullName("Author_2").orElse(null),
                        genreRepository.findAllByNameIn(Set.of("Genre_3", "Genre_4"))),
                new Book("BookTitle_3",
                        authorRepository.findByFullName("Author_3").orElse(null),
                        genreRepository.findAllByNameIn(Set.of("Genre_5", "Genre_6"))));
        bookRepository.saveAll(books);
    }

    @ChangeSet(order = "005", id = "insertComments", author = "mch", runAlways = true)
    public void insertComments(CommentRepository commentRepository, BookRepository bookRepository) {
        List<Comment> comments = List.of(
                new Comment("Wonderful. Wonderful piece of art. Worth your time.",
                        bookRepository.findByTitle("BookTitle_1").map(Book::getId).orElse(null)),
                new Comment("A good piece for the evening",
                        bookRepository.findByTitle("BookTitle_2").map(Book::getId).orElse(null)),
                new Comment("How can you read this?",
                        bookRepository.findByTitle("BookTitle_1").map(Book::getId).orElse(null)));
        commentRepository.saveAll(comments);
    }
}
