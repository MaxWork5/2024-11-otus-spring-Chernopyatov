package ru.otus.hw.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.services.BookService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping(value = "/api/books", params = "id")
    public ResponseEntity<BookDto> showBook(@RequestParam Long id) {
        var book = bookService.findById(id)
                .orElseThrow(NotFoundException::new);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/api/books")
    public List<BookDto> showAllBooks() {
        List<BookDto> books = bookService.findAll();
        if (books.isEmpty()) {
            throw new NotFoundException();
        }
        return books;
    }

    @PostMapping("/api/books")
    public ResponseEntity<BookDto> saveBook(@Valid @RequestBody BookDto dto) {
        return ResponseEntity.ok(bookService.insert(dto.getTitle(), dto.getAuthor().getId(), dto.getGenreIds()));
    }

    @PutMapping("/api/books")
    public ResponseEntity<BookDto> updateBook(@Valid @RequestBody BookDto book) {
        return ResponseEntity.ok(bookService.update(book.getId(),
                book.getTitle(),
                book.getAuthor().getId(),
                book.getGenreIds()));
    }

    @DeleteMapping(value = "/api/books", params = "id")
    public void deleteBook(@RequestParam Long id) {
        bookService.deleteById(id);
        List<BookDto> books = bookService.findAll();
        if (books.isEmpty()) {
            throw new NotFoundException();
        }
    }
}