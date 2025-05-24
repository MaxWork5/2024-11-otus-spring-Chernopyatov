package ru.otus.hw.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Set;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    private final GenreService genreService;

    private final AuthorService authorService;

    @GetMapping("/find")
    public String showId(Model model) {
        model.addAttribute("id", 0);
        return "find";
    }

    @GetMapping(value = "/find", params = "id")
    public String showBook(@RequestParam Long id, Model model) {
        BookDto book = bookService.findById(id)
                .orElseThrow(NotFoundException::new);
        model.addAttribute("books", book);
        return "list";
    }

    @GetMapping
    public String showAllBooks(Model model) {
        List<BookDto> books = bookService.findAll();
        model.addAttribute("books", books);
        return "list";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        BookDto book = bookService.findById(id)
                .orElseThrow(NotFoundException::new);
        model.addAttribute("book", book);
        model.addAttribute("allGenres", genreService.findAll());
        model.addAttribute("allAuthors", authorService.findAll());

        return "edit";
    }

    @GetMapping("/insert")
    public String insertPage(Model model) {
        var book = new BookDto(0,"",new AuthorDto(0,""),List.of(), Set.of());
        model.addAttribute("book", book);
        model.addAttribute("allGenres", genreService.findAll());
        model.addAttribute("allAuthors", authorService.findAll());

        return "insert";
    }

    @PostMapping("/insert")
    public String saveBook(@Valid @ModelAttribute("book") BookDto book) {
        bookService.insert(book.getTitle(),
                book.getAuthor().getId(),
                book.getGenreIds());
        return "redirect:/";
    }

    @PatchMapping("/edit")
    public String updateBook(@Valid @ModelAttribute("book") BookDto book) {
        bookService.update(book.getId(), book.getTitle(), book.getAuthor().getId(), book.getGenreIds());
        return "redirect:/";
    }

    @DeleteMapping("/delete")
    public String deleteBook(@RequestParam Long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }
}
