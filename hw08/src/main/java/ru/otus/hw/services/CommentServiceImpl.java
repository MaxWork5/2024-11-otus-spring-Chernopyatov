package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<CommentDto> findById(String id) {
        return commentRepository.findById(id)
                .map(CommentDto::fromDomainObject);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findByBookId(String bookId) {
        var variable = commentRepository.findAllByBookId(bookId).stream()
                .map(CommentDto::fromDomainObject)
                .toList();

        if (variable.isEmpty()) {
            throw new EntityNotFoundException("No comment found for book with id " + bookId);
        }

        return variable;
    }

    @Transactional
    @Override
    public CommentDto insert(String description, String bookId) {
        var savedEntity = save(null, description, bookId);
        return CommentDto.fromDomainObject(savedEntity);
    }

    @Transactional
    @Override
    public CommentDto update(String id, String description, String bookId) {
        var savedEntity = save(id, description, bookId);
        return CommentDto.fromDomainObject(savedEntity);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    private Comment save(String id, String description, String bookId) {
        if (bookRepository.findById(bookId).isEmpty()) {
            throw new EntityNotFoundException("Book with id %s not found".formatted(bookId));
        }
        var comment = new Comment(description, bookId);
        comment.setId(id);
        return commentRepository.save(comment);
    }
}
