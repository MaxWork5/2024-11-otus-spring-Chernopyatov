package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations jdbc;

    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> findById(long id) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        var books = jdbc
                .query("select b.id, b.title, b.author_id, a.full_name, bg.genre_id, g.name from books b " +
                                "left join authors a on b.author_id = a.id " +
                                "left join books_genres bg on b.id = bg.book_id " +
                                "left join genres g on bg.genre_id = g.id where b.id = :id",
                        params, new BookResultSetExtractor());
        return Optional.ofNullable(books);
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        findById(id)
                .ifPresent(book -> {
                    removeGenresRelationsFor(book);
                    jdbc.update("delete from books where id = :id", params);
                });
    }

    private List<Book> getAllBooksWithoutGenres() {
        return jdbc.query("select b.id, b.title, b.author_id, a.full_name from books b " +
                        "left join authors a on b.author_id = a.id",
                new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbc.query("select book_id, genre_id from books_genres", new BookGenreRelationRowMapper());
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        // Добавить книгам (booksWithoutGenres) жанры (genres) в соответствии со связями (relations)
        var booksByIdentifier = booksWithoutGenres.stream().collect(Collectors.toMap(t -> t.getId(), t -> t));
        var genresByIdentifier = genres.stream().collect(Collectors.toMap(t -> t.getId(), t -> t));
        booksWithoutGenres = relations.stream().map(t -> {
            var book = booksByIdentifier.get(t.bookId());
            List<Genre> genre = new ArrayList<Genre>();
            if (!(book.getGenres() == null)) {
                genre = book.getGenres();
            }
            genre.add(genresByIdentifier.get(t.genreId()));
            book.setGenres(genre);
            return book;
        }).toList();
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());

        //noinspection DataFlowIssue
        jdbc.update("insert into books (title, author_id) values(:title, :author_id)",
                params, keyHolder, new String[]{"id"});
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", book.getId());
        params.put("title", book.getTitle());
        params.put("author_id", book.getAuthor().getId());

        // Выбросить EntityNotFoundException если не обновлено ни одной записи в БД
        findById(book.getId())
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + book.getId() + " not found"));

        jdbc.update("update books set title = :title, author_id = :author_id where id = :id", params);

        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        // Использовать метод batchUpdate
        List<SqlParameterSource> params = new ArrayList<>();
        var bookId = book.getId();
        for (Genre genre : book.getGenres()) {
            MapSqlParameterSource source = new MapSqlParameterSource("genre_id",
                    genre.getId()).addValue("book_id", bookId);
            params.add(source);
        }
        jdbc.batchUpdate("insert into books_genres (book_id, genre_id) values(:book_id, :genre_id)",
                params.toArray(new SqlParameterSource[params.size()]));
        params.clear();
    }

    private void removeGenresRelationsFor(Book book) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", book.getId());
        jdbc.update("delete from books_genres where book_id = :id", params);
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String title = rs.getString("title");
            long authorId = rs.getLong("author_id");
            String fullName = rs.getString("full_name");
            return new Book(id, title, new Author(authorId, fullName), null);
        }
    }

    private static class BookGenreRelationRowMapper implements RowMapper<BookGenreRelation> {

        @Override
        public BookGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
            long bookId = rs.getLong("book_id");
            long genreId = rs.getLong("genre_id");
            return new BookGenreRelation(bookId, genreId);
        }
    }

    // Использовать для findById
    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            var book = new Book();
            var genre = new ArrayList<Genre>();
            if (!rs.next()) {
                return null;
            } else {
                do {
                    if (genre.isEmpty()) {
                        book.setId(rs.getLong("id"));
                        book.setTitle(rs.getString("title"));
                        book.setAuthor(new Author(rs.getLong(("author_id")), rs.getString("full_name")));
                    }
                    genre.add(new Genre(rs.getLong("genre_id"), rs.getString("name")));
                } while (rs.next());
                book.setGenres(genre);
                return book;
            }
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}