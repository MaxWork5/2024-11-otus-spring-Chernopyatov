package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.GenreDto;

@Component
public class GenreConverter {
    public String genreToString(GenreDto dto) {
        return "Id: %d, Name: %s".formatted(dto.getId(), dto.getName());
    }
}