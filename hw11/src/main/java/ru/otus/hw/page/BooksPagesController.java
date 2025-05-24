package ru.otus.hw.page;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
@SuppressWarnings("all")
public class BooksPagesController {

    private final BookService bookService;

    private final GenreService genreService;

    private final AuthorService authorService;

    @GetMapping
    public String listBooksPage() {
        return "list";
    }

    @GetMapping("/edit/{id}")
    public String editBooksPage(@PathVariable String id, Model model) {
        model.addAttribute("book", bookService.findById(id)
                .map(book -> {
                    book.setGenreIds(book.getGenres().stream()
                            .map(GenreDto::getId)
                            .collect(Collectors.toSet()));
                    return book;
                }));

        model.addAttribute("allGenres", genreService.findAll());
        model.addAttribute("allAuthors", authorService.findAll());

        return "edit";
    }

    @GetMapping("/find")
    public String findBooksPage() {
        return "find";
    }

    @GetMapping("/insert")
    public String insertBooksPage(Model model) {
        model.addAttribute("allGenres", genreService.findAll());
        model.addAttribute("allAuthors", authorService.findAll());

        return "insert";
    }
}
