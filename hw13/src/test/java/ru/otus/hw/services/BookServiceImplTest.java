package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.otus.hw.configuration.AclConfiguration;
import ru.otus.hw.configuration.MyUserDetails;
import ru.otus.hw.domain.User;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("Интеграционный тест сервиса для работы с книгами")
@DataJpaTest(excludeAutoConfiguration = {TransactionAutoConfiguration.class})
@Import(AclConfiguration.class)
class BookServiceImplTest {
    @Autowired
    MutableAclService mutableAclService;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    GenreRepository genreRepository;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    CommentRepository commentRepository;

    AclServiceWrapperService aclServiceWrapperService;

    BookServiceImpl service;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(new
                MyUserDetails(new User(2L, "user", "$2a$16$dm.iWNNbh/7bOxp71k2Z..KshkayiZDZRA5pp8X0tezAMr3mTpvti", "USER")),
                null, List.of(new SimpleGrantedAuthority("ROLE_USER"))));
        aclServiceWrapperService = new AclServiceWrapperServiceImpl(mutableAclService);
        service = new BookServiceImpl(aclServiceWrapperService, authorRepository, commentRepository, genreRepository, bookRepository);
    }

    @DisplayName("должен загрузить DTO книги из Базы Данных по его идентификатору")
    @Test
    void findById() {
        assertThat(service.findById(1))
                .isEqualTo(Optional.of(new BookDto(1,
                        "BookTitle_1",
                        new AuthorDto(1, "Author_1"),
                        List.of(new GenreDto(1, "Genre_1"), new GenreDto(2, "Genre_2")), null)));
    }

    @DisplayName("должен загрузить все DTO книг")
    @Test
    void findAll() {
        var expectedBooks = bookRepository.findAll().stream().map(book ->
                new BookDto(
                        book.getId(),
                        book.getTitle(),
                        new AuthorDto(
                                book.getAuthor().getId(),
                                book.getAuthor().getFullName()),
                        book.getGenres().stream()
                                .map(genre -> new GenreDto(genre.getId(), genre.getName())).toList(),
                        null)).toList();

        assertThat(service.findAll()).isNotNull().usingRecursiveComparison().isEqualTo(expectedBooks);
    }

    @DisplayName("должен загрузить новую книгу в Базу Данных")
    @Test
    void insert() {
        var savedBook = service.insert("Test_Book_1", 2, Set.of(1L, 4L));
        var baseBook = service.findById(savedBook.getId());
        assertThat(baseBook)
                .get()
                .isEqualTo(savedBook);
    }

    @DisplayName("должен отбросить ошибку при попытке загрузить книгу в Базу Данных")
    @Test
    void insertThrowEntityNotFoundException() {
        assertThatThrownBy(() -> service.insert("Test_Book_1", 2, Set.of(1L, 40L)))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("должен отбросить ошибку при попытке записать книгу в Базе Данных не записав жанры")
    @Test
    void insertThrowIllegalArgumentException() {
        assertThatThrownBy(() -> service.insert("Test_Book_1", 20, Set.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("должен изменить книгу в Базе Данных")
    @Test
    void update() {
        var savedBook = service.update(2, "Test_Book_1", 2, Set.of(1L, 4L));
        var baseBook = service.findById(2);
        assertThat(baseBook)
                .get()
                .isEqualTo(savedBook);
    }

    @DisplayName("должен отбросить ошибку при попытке обновить книгу в Базе Данных")
    @Test
    void updateThrowEntityNotFoundException() {
        assertThatThrownBy(() -> service.update(2, "Test_Book_1", 20, Set.of(1L, 4L)))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("должен отбросить ошибку при попытке обновить книгу в Базе Данных не записав жанры")
    @Test
    void updateThrowIllegalArgumentException() {
        assertThatThrownBy(() -> service.update(2,"Test_Book_1", 20, Set.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("должен удалить книгу в Базе Данных")
    @Test
    void deleteById() {
        var beforeDeleteBook = service.findById(3);
        service.deleteById(3);
        var afterDeleteBook = service.findById(3);
        assertThat(beforeDeleteBook).isNotNull().isNotEmpty();
        assertThat(afterDeleteBook).isNotNull().isEmpty();
    }
}