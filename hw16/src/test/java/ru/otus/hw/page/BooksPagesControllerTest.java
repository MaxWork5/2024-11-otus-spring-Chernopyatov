package ru.otus.hw.page;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@DisplayName("Контроллер библиотеки")
@WebMvcTest(controllers = BooksPagesController.class)
class BooksPagesControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private GenreService genreService;
    @MockitoBean
    private AuthorService authorService;
    @MockitoBean
    private BookService service;

    private final List<GenreDto> genres = List.of(new GenreDto(1L,"Genre_1"), new GenreDto(2L,"Genre_2"),
            new GenreDto(3L,"Genre_3"), new GenreDto(4L,"Genre_4"),
            new GenreDto(5L,"Genre_5"), new GenreDto(6L,"Genre_6"));

    private final List<AuthorDto> authors = List.of(new AuthorDto(1L,"Author_1"),
            new AuthorDto(2L,"Author_2"), new AuthorDto(3L,"Author_3"));

    @DisplayName("должен вернуть страницу всех книг")
    @Test
    void listBooksPage() throws Exception {
        mvc.perform(get("/"))
                .andExpect(view().name("list"));
    }

    @DisplayName("должен вернуть страницу изменения книги с моделью всех жанров, всех авторов и изменяемой книги")
    @Test
    void editBooksPage() throws Exception {
        var id = 1L;
        var book = new BookDto(id,"Title",new AuthorDto(1L,"Author_1"),
                List.of(new GenreDto(1L, "Genre_1"), new GenreDto(2L,"Genre_2")), Set.of(1L,2L));
        when(service.findById(id))
                .thenReturn(Optional.of(new BookDto(id,"Title",new AuthorDto(1L,"Author_1"),
                        List.of(new GenreDto(1L, "Genre_1"), new GenreDto(2L,"Genre_2")),null)));
        when(authorService.findAll()).thenReturn(authors);
        when(genreService.findAll()).thenReturn(genres);
        mvc.perform(get("/edit/{id}",id))
                .andExpect(model().attribute("book",book))
                .andExpect(model().attribute("allGenres",genres))
                .andExpect(model().attribute("allAuthors",authors))
                .andExpect(view().name("edit"));
    }

    @DisplayName("должен вернуть страницу ошибку при попытке изменить данные несуществующей книги")
    @Test
    void editPageThrowException() throws Exception {
        var id = 1L;
        when(service.findById(id))
                .thenThrow(new NotFoundException());

        mvc.perform(get("/edit/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string("The books was not found"));
    }

    @DisplayName("должен загрузить страницу поиска книги по идентификатору")
    @Test
    void findBooksPage() throws Exception {
        mvc.perform(get("/find"))
                .andExpect(view().name("find"));
    }

    @DisplayName("должен вернуть страницу ввода новой книги с моделью всех жанров и всех авторов")
    @Test
    void insertBooksPage() throws Exception {
        when(authorService.findAll()).thenReturn(authors);
        when(genreService.findAll()).thenReturn(genres);
        mvc.perform(get("/insert"))
                .andExpect(model().attribute("allGenres",genres))
                .andExpect(model().attribute("allAuthors",authors))
                .andExpect(view().name("insert"));
    }
}