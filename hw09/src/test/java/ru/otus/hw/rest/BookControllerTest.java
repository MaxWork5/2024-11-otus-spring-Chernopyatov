package ru.otus.hw.rest;

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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Контроллер библиотеки")
@WebMvcTest(controllers = BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private GenreService genreService;
    @MockitoBean
    private AuthorService authorService;
    @MockitoBean
    private BookService service;

    @DisplayName("должен загрузить страницу поиска книги по идентификатору с моделью идентификатора")
    @Test
    void showId() throws Exception {
        mvc.perform(get("/find"))
                .andExpect(view().name("find"))
                .andExpect(model().attribute("id",0));
    }

    @DisplayName("должен вернуть страницу книги по указанному идентификатору книги")
    @Test
    void showBook() throws Exception {
        var id = 1L;
        when(service.findById(id))
                .thenReturn(Optional.of(new BookDto(
                        id,
                        "Title",
                        new AuthorDto(
                                1L,
                                "Author_1"
                        ),
                        List.of(
                                new GenreDto(
                                        1L,
                                        "Genre_1"
                                ),
                                new GenreDto(
                                        2L,
                                        "Genre_2"
                                )
                        ),
                        null)));

        mvc.perform(get("/find").param("id","1"))
                .andExpect(view().name("list"))
                .andExpect(model().attribute("books",
                        new BookDto(
                                id,
                                "Title",
                                new AuthorDto(
                                        1L,
                                        "Author_1"),
                                List.of(new GenreDto(
                                        1L,
                                        "Genre_1"
                                        ),
                                        new GenreDto(
                                                2L,
                                                "Genre_2"
                                        )
                                ),
                                null)));
    }

    @DisplayName("должен вернуть страницу ошибки при указании несуществующего идентификатора книги")
    @Test
    void showBookThrowException() throws Exception {
        var id = 1L;
        when(service.findById(id))
                .thenThrow(new NotFoundException());

        mvc.perform(get("/find").param("id","1"))
                .andExpect(view().name("customerError"));
    }

    @DisplayName("должен вернуть страницу всех книг")
    @Test
    void showAllBooks() throws Exception {
        when(service.findAll()).thenReturn(List.of(
                new BookDto(
                        1L,
                        "Title_1",
                        new AuthorDto(
                                1L,
                                "Author_1"
                        ),
                        List.of(
                                new GenreDto(
                                        1L,
                                        "Genre_1"
                                ),
                                new GenreDto(
                                        2L,
                                        "Genre_2"
                                )
                        ),
                        null
                ),
                new BookDto(
                        2L,
                        "Title_2",
                        new AuthorDto(
                                2L,
                                "Author_2"
                        ),
                        List.of(
                                new GenreDto(
                                        3L,
                                        "Genre_3"
                                ),
                                new GenreDto(
                                        4L,
                                        "Genre_4"
                                )
                        ),
                        null
                ),
                new BookDto(
                        3L,
                        "Title_3",
                        new AuthorDto(
                                3L,
                                "Author_3"
                        ),
                        List.of(
                                new GenreDto(
                                        5L,
                                        "Genre_5"
                                ),
                                new GenreDto(
                                        6L,
                                        "Genre_6"
                                )
                        ),
                        null
                )
        ));

        mvc.perform(get("/"))
                .andExpect(view().name("list"))
                .andExpect(model().attribute("books",List.of(
                        new BookDto(
                                1L,
                                "Title_1",
                                new AuthorDto(
                                        1L,
                                        "Author_1"
                                ),
                                List.of(
                                        new GenreDto(
                                                1L,
                                                "Genre_1"
                                        ),
                                        new GenreDto(
                                                2L,
                                                "Genre_2"
                                        )
                                ),
                                null),
                        new BookDto(
                                2L,
                                "Title_2",
                                new AuthorDto(
                                        2L,
                                        "Author_2"
                                ),
                                List.of(
                                        new GenreDto(
                                                3L,
                                                "Genre_3"
                                        ),
                                        new GenreDto(
                                                4L,
                                                "Genre_4"
                                        )
                                ),
                                null),
                        new BookDto(
                                3L,
                                "Title_3",
                                new AuthorDto(
                                        3L,
                                        "Author_3"
                                ),
                                List.of(
                                        new GenreDto(
                                                5L,
                                                "Genre_5"
                                        ),
                                        new GenreDto(
                                                6L,
                                                "Genre_6"
                                        )
                                ),
                                null))));
    }

    @DisplayName("должен вернуть страницу изменения книги с моделью всех жанров, всех авторов и изменяемой книги")
    @Test
    void editPage() throws Exception{
        var id = 1L;
        when(service.findById(id)).thenReturn(Optional.of(
                new BookDto(
                        id,
                        "Title",
                        new AuthorDto(
                                1L,
                                "Author_1"
                        ),
                        List.of(
                                new GenreDto(
                                        1L, "Genre_1"
                                ),
                                new GenreDto(
                                        2L,
                                        "Genre_2"
                                )
                        ),
                        null)));
        when(authorService.findAll()).thenReturn(List.of(
                new AuthorDto(
                        1L,
                        "Author_1"
                ),
                new AuthorDto(
                        2L,
                        "Author_2"
                ),
                new AuthorDto(
                        3L,
                        "Author_3"
                )));
        when(genreService.findAll()).thenReturn(List.of(
                new GenreDto(
                        1L,
                        "Genre_1"
                ),
                new GenreDto(
                        2L,
                        "Genre_2"
                ),
                new GenreDto(
                        3L,
                        "Genre_3"
                ),
                new GenreDto(
                        4L,
                        "Genre_4"
                ),
                new GenreDto(
                        5L,
                        "Genre_5"
                ),
                new GenreDto(
                        6L,
                        "Genre_6"
                )));

        mvc.perform(get("/edit/{id}",id)).andExpect(view().name("edit"))
                .andExpect(model().attribute("book",new BookDto(
                        id,
                        "Title",
                        new AuthorDto(
                                1L,
                                "Author_1"
                        ),
                        List.of(
                                new GenreDto(
                                        1L,
                                        "Genre_1"
                                ),
                                new GenreDto(
                                        2L,
                                        "Genre_2"
                                )
                        ),
                        null)))
                .andExpect(model().attribute("allGenres",List.of(
                        new GenreDto(
                                1L,
                                "Genre_1"
                        ),
                        new GenreDto(
                                2L,
                                "Genre_2"
                        ),
                        new GenreDto(
                                3L,
                                "Genre_3"
                        ),
                        new GenreDto(
                                4L,
                                "Genre_4"
                        ),
                        new GenreDto(
                                5L,
                                "Genre_5"
                        ),
                        new GenreDto(
                                6L,
                                "Genre_6"
                        ))))
                .andExpect(model().attribute("allAuthors", List.of(
                        new AuthorDto(
                                1L,
                                "Author_1"
                        ),
                        new AuthorDto(
                                2L,
                                "Author_2"
                        ),
                        new AuthorDto(
                                3L,
                                "Author_3"
                        ))));
    }

    @DisplayName("должен вернуть страницу ошибку при попытке изменить данные несуществующей книги")
    @Test
    void editPageThrowException() throws Exception {
        var id = 1L;
        when(service.findById(id))
                .thenThrow(new NotFoundException());

        mvc.perform(get("/find").param("id","1"))
                .andExpect(view().name("customerError"));
    }

    @DisplayName("должен вернуть страницу ввода новой книги с моделью всех жанров и всех авторов")
    @Test
    void insertPage() throws Exception{
        when(authorService.findAll()).thenReturn(List.of(
                new AuthorDto(
                        1L,
                        "Author_1"
                ),
                new AuthorDto(
                        2L,
                        "Author_2"
                ),
                new AuthorDto(
                        3L,
                        "Author_3"
                )));
        when(genreService.findAll()).thenReturn(List.of(
                new GenreDto(
                        1L,
                        "Genre_1"
                ),
                new GenreDto(
                        2L,
                        "Genre_2"
                ),
                new GenreDto(
                        3L,
                        "Genre_3"
                ),
                new GenreDto(
                        4L,
                        "Genre_4"
                ),
                new GenreDto(
                        5L,
                        "Genre_5"
                ),
                new GenreDto(
                        6L,
                        "Genre_6"
                )));

        mvc.perform(get("/insert")).andExpect(view().name("insert"))
                .andExpect(model().attribute("book",
                        new BookDto(
                                0,
                                "",
                                new AuthorDto(
                                        0,
                                        ""
                                ),
                                List.of(),
                                Set.of()
                        )))
                .andExpect(model().attribute("allGenres",List.of(
                        new GenreDto(
                                1L,
                                "Genre_1"
                        ),
                        new GenreDto(
                                2L,
                                "Genre_2"
                        ),
                        new GenreDto(
                                3L,
                                "Genre_3"
                        ),
                        new GenreDto(
                                4L,
                                "Genre_4"
                        ),
                        new GenreDto(
                                5L,
                                "Genre_5"
                        ),
                        new GenreDto(
                                6L,
                                "Genre_6"
                        ))))
                .andExpect(model().attribute("allAuthors", List.of(
                        new AuthorDto(
                                1L,
                                "Author_1"),
                        new AuthorDto(
                                2L,
                                "Author_2"
                        ),
                        new AuthorDto(
                                3L,
                                "Author_3"
                        ))));

    }

    @DisplayName("должен вернуть страницу всех книг после сохранения новой книги")
    @Test
    void saveBook() throws Exception {
        mvc.perform(post("/insert")
                .param("id","0")
                .param("title","Title")
                .param("author.id","1")
                .param("author.fullName",(String) null)
                .param("genres",(String) null)
                .param("genreIds","1, 3"))
                .andExpect(view().name("redirect:/"));
        verify(service, times(1)).insert("Title",1L, Set.of(1L,3L));
    }

    @DisplayName("должен вернуть страницу всех книг после изменения книги")
    @Test
    void updateBook() throws Exception {
        mvc.perform(patch("/edit")
                        .param("id","1")
                        .param("title","Title")
                        .param("author.id","1")
                        .param("author.fullName",(String) null)
                        .param("genres",(String) null)
                        .param("genreIds","1, 3"))
                .andExpect(view().name("redirect:/"));
        verify(service, times(1)).update(1L,"Title",1L, Set.of(1L,3L));
    }

    @DisplayName("должен вернуть страницу всех книг при удалении книги")
    @Test
    void deleteBook() throws Exception{
        mvc.perform(delete("/delete").param("id","1"))
                .andExpect(view().name("redirect:/"));
    }
}