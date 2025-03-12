package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<CommentDto> findById(String id);

    List<CommentDto> findByBookId(String bookId);

    CommentDto insert(String description, String bookId);

    CommentDto update(String id, String description, String bookId);

    void deleteById(String id);
}
