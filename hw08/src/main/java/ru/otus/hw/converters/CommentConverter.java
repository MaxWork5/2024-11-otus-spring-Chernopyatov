package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;

@RequiredArgsConstructor
@Component
public class CommentConverter {
    private final BookConverter bookConverter;

    public String commentToString(CommentDto dto) {
        return "Id: %s, description: %s, book Id: %s".formatted(dto.getId(), dto.getDescription(), dto.getBookId());
    }
}
