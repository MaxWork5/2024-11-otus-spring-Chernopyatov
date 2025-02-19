package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Book;

import java.util.List;

@Data
@AllArgsConstructor
public class BookDto {
    private long id;

    private String title;

    private AuthorDto author;

    private List<GenreDto> genres;

    public static BookDto fromDomainObject(Book book) {
        return new BookDto(book.getId(),
                book.getTitle(),
                AuthorDto.fromDomainObject(book.getAuthor()),
                book.getGenres().stream()
                        .map(GenreDto::fromDomainObject)
                        .toList());
    }
}
