package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.entities.jpa.JpaBook;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public String bookToString(JpaBook dto) {
        return "Id: %d, title: %s, author: {%s}, genres: [%s]".formatted(
                dto.getId(),
                dto.getTitle(),
                authorConverter.authorToString(dto.getAuthor()),
                dto.getGenres().stream()
                        .map(genreConverter::genreToString)
                        .map("{%s}"::formatted)
                        .collect(Collectors.joining(", ")));
    }
}