package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.domain.Book;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public Mono<BookDto> findById(String id) {
        return bookRepository.findById(id)
                .map(BookDto::fromDomainObject)
                .switchIfEmpty(Mono.error(new NotFoundException()));
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<BookDto> findAll() {
        return bookRepository.findAll()
                .map(BookDto::fromDomainObject);
    }

    @Transactional
    @Override
    public Mono<BookDto> insert(String title, String authorId, Set<String> genresIds) {
        return save(null, title, authorId, genresIds).map(BookDto::fromDomainObject);
    }

    @Transactional
    @Override
    public Mono<BookDto> update(String id, String title, String authorId, Set<String> genresIds) {
        return save(id, title, authorId, genresIds).map(BookDto::fromDomainObject);
    }

    @Transactional
    @Override
    public Mono<Void> deleteById(String id) {
        return bookRepository.deleteById(id);
    }

    private Mono<Book> save(String id, String title, String authorId, Set<String> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        return authorRepository.findById(authorId)
                .flatMap(author ->
                        genreRepository.findAllByIdIn(genresIds)
                                .collectList()
                                .flatMap(genres -> {
                                    if (genres.isEmpty()) {
                                        return Mono.error(
                                                new EntityNotFoundException("One or all genres with ids %s not found"
                                                        .formatted(genresIds)));
                                    }

                                    var book = new Book(title, author, genres);
                                    book.setId(id);
                                    return bookRepository.save(book);
                                })
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Author with id %s not found"
                        .formatted(authorId)))));
    }
}