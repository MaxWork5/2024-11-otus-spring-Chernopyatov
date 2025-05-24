package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Rest-контроллер библиотеки")
@WebMvcTest(controllers = BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private BookService service;

    private final List<BookDto> books = List.of(
            new BookDto(1L, "Title_1", new AuthorDto(1L, "Author_1"),
                    List.of(new GenreDto(1L, "Genre_1"), new GenreDto(2L, "Genre_2")),null),
            new BookDto(2L, "Title_2", new AuthorDto(2L, "Author_2"),
                    List.of(new GenreDto(3L, "Genre_3"), new GenreDto(4L, "Genre_4")),null),
            new BookDto(3L, "Title_3", new AuthorDto(3L, "Author_3"),
                    List.of(new GenreDto(5L, "Genre_5"), new GenreDto(6L, "Genre_6")),null));

    @DisplayName("должен загрузить DTO книги по его идентификатору и вернуть код 200 с DTO")
    @Test
    void showBook() throws Exception {
        given(service.findById(2L)).willReturn(Optional.of(books.get(1)));

        mvc.perform(get("/api/books").param("id","2"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(books.get(1))));
    }

    @DisplayName("должен отбросить ошибку при не нахождении книги по идентификатору и вернуть код 404 с сообщением")
    @Test
    void showBookThrowsException() throws Exception {
        given(service.findById(4L)).willReturn(Optional.empty());

        mvc.perform(get("/api/books").param("id","4"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("The books was not found"));
    }

    @DisplayName("должен загрузить все DTO книг и вернуть код 200 со списком DTO")
    @Test
    void showAllBooks() throws Exception {
        given(service.findAll()).willReturn(books);

        mvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(books)));
    }

    @DisplayName("должен отбросить ошибку при не нахождении книг и вернуть код 404 с сообщением")
    @Test
    void showAllBooksThrowsException() throws Exception {
        given(service.findAll()).willReturn(List.of());

        mvc.perform(get("/api/books"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("The books was not found"));
    }

    @DisplayName("должен сохранить книгу по полученному DTO и вернуть 200 с DTO сохранённой книги")
    @Test
    void saveBook() throws Exception {
        var expectedResult = new BookDto(4, "Title", new AuthorDto(2L, "Author_2"),
                List.of(new GenreDto(5L, "Genre_5")), null);
        var dto = new BookDto(0, "Title", new AuthorDto(2L, null), List.of(), Set.of(5L));

        given(service.insert("Title", 2L, Set.of(5L)))
                .willReturn(expectedResult);

        mvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedResult)));
    }

    @DisplayName("должен отбросить ошибку при сохранении книги из-за отсутствия идентификаторов жанров и вернуть код 404 с сообщением")
    @Test
    void saveBookThrowsIllegalArgument() throws Exception {
        var dto = new BookDto(0, "Title", new AuthorDto(2L, null), List.of(), Set.of());

        given(service.insert("Title", 2L, Set.of())).willThrow(IllegalArgumentException.class);

        mvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cannot transfer a book without specifying the genres"));
    }

    @DisplayName("должен отбросить ошибку при сохранении книги из-за указания идентификатора автора, которого нет Базе Данных, и вернуть код 404 с сообщением")
    @Test
    void saveBookThrowsEntityNotFound() throws Exception {
        var dto = new BookDto(0, "Title", new AuthorDto(6L, null), List.of(), Set.of(5L));

        given(service.insert("Title", 6L, Set.of(5L))).willThrow(new EntityNotFoundException("Author with id 6 not found"));

        mvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Author with id 6 not found"));
    }

    @DisplayName("должен изменить книгу по полученному DTO и вернуть 200 с DTO изменённой книги")
    @Test
    void updateBook() throws Exception {
        var expectedResult = new BookDto(4, "Title", new AuthorDto(2L, "Author_2"), List.of(new GenreDto(5L, "Genre_5")), null);
        var dto = new BookDto(4, "Title", new AuthorDto(2L, null), List.of(), Set.of(5L));

        given(service.update(4,"Title", 2L, Set.of(5L)))
                .willReturn(expectedResult);

        mvc.perform(put("/api/books").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedResult)));
    }

    @DisplayName("должен отбросить ошибку при изменении книги из-за отсутствия идентификаторов жанров и вернуть код 404 с сообщением")
    @Test
    void updateBookThrowsIllegalArgument() throws Exception {
        var dto = new BookDto(4, "Title", new AuthorDto(2L, null), List.of(), Set.of());

        given(service.update(4L,"Title", 2L, Set.of())).willThrow(IllegalArgumentException.class);

        mvc.perform(put("/api/books").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cannot transfer a book without specifying the genres"));
    }

    @DisplayName("должен отбросить ошибку при изменении книги из-за указания идентификатора жанра, которого нет Базе Данных и вернуть код 404 с сообщением")
    @Test
    void updateBookThrowsEntityNotFound() throws Exception {
        var dto = new BookDto(4, "Title", new AuthorDto(2L, null), List.of(), Set.of(15L));

        given(service.update(4L, "Title", 2L, Set.of(15L))).willThrow(new EntityNotFoundException("One or all genres with ids 15 not found"));

        mvc.perform(put("/api/books").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("One or all genres with ids 15 not found"));
    }

    @DisplayName("должен удалить книгу и вернуть код 200")
    @Test
    void deleteBook() throws Exception {
        given(service.findAll()).willReturn(books);

        mvc.perform(delete("/api/books").param("id","4"))
                .andExpect(status().isOk());
    }

    @DisplayName("должен отбросить ошибку при удалении книги и не нахождении других и вернуть код 404 с сообщением")
    @Test
    void deleteBookThrowsNotFound() throws Exception {
        given(service.findAll()).willReturn(List.of());

        mvc.perform(delete("/api/books").param("id","4"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("The books was not found"));
    }
}