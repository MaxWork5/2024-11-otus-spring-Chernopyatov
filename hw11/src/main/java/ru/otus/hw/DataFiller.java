package ru.otus.hw;

import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import ru.otus.hw.domain.Author;
import ru.otus.hw.domain.Book;
import ru.otus.hw.domain.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class DataFiller implements ApplicationRunner {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    public void run(ApplicationArguments args) {
        authorRepository.deleteAll().subscribe();
        genreRepository.deleteAll().subscribe();
        bookRepository.deleteAll().subscribe();

        Flux.just(new Author("Author_1"), new Author("Author_2"), new Author("Author_3"))
                .flatMap(authorRepository::save)
                .collectList()
                .subscribe(authors -> Flux.just(new Genre("Genre_1"),
                                new Genre("Genre_2"),
                                new Genre("Genre_3"),
                                new Genre("Genre_4"),
                                new Genre("Genre_5"),
                                new Genre("Genre_6"))
                        .flatMap(genreRepository::save)
                        .collectList()
                        .subscribe(genres -> prepareBooks(genres, authors)
                                .flatMap(bookRepository::save)
                                .subscribe()));
    }

    private Flux<Book> prepareBooks(List<Genre> genreList, List<Author> authorList) {
        return Flux.just(new Book("BookTitle_1", authorList.stream()
                        .filter(author -> "Author_1".equals(author.getFullName()))
                        .findFirst()
                        .orElse(null),
                        genreList.stream()
                                .filter(genre -> Set.of("Genre_1", "Genre_2").contains(genre.getName()))
                                .toList()),
                new Book("BookTitle_2", authorList.stream()
                        .filter(author -> "Author_2".equals(author.getFullName()))
                        .findFirst()
                        .orElse(null),
                        genreList.stream()
                                .filter(genre -> Set.of("Genre_3", "Genre_4").contains(genre.getName()))
                                .toList()),
                new Book("BookTitle_3", authorList.stream()
                        .filter(author -> "Author_3".equals(author.getFullName()))
                        .findFirst()
                        .orElse(null),
                        genreList.stream()
                                .filter(genre -> Set.of("Genre_5", "Genre_6").contains(genre.getName()))
                                .toList()));
    }
}