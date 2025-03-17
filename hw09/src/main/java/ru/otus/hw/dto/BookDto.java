package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.domain.Book;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@Data
@AllArgsConstructor
public class BookDto {
    private long id;

    private String title;

    private AuthorDto author;

    private List<GenreDto> genres;

    private Set<Long> genreIds;

    public static BookDto fromDomainObject(Book book) {
        return new BookDto(book.getId(),
                book.getTitle(),
                AuthorDto.fromDomainObject(book.getAuthor()),
                book.getGenres().stream()
                        .map(GenreDto::fromDomainObject)
                        .toList(),
                null);
    }

    public String genreListToString() {
        if (isEmpty(genres)) {
            return "";
        }

        return String.join(", ", genres.stream().map(GenreDto::getName).collect(Collectors.toSet()));
    }
}