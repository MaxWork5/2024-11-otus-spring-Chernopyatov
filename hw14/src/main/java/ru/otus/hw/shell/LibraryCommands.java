package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.cache.MongoJpaCache;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.repositories.jpa.JpaAuthorRepository;
import ru.otus.hw.repositories.jpa.JpaBookRepository;
import ru.otus.hw.repositories.jpa.JpaCommentRepository;
import ru.otus.hw.repositories.jpa.JpaGenreRepository;

import java.util.Properties;
import java.util.stream.Collectors;

import static ru.otus.hw.configuration.JobConfiguration.IMPORT_LIBRARY_JOB_NAME;

@SuppressWarnings({"unused"})
@RequiredArgsConstructor
@ShellComponent
public class LibraryCommands {

    private static long counter = 0;

    private final JpaAuthorRepository jpaAuthorRepository;

    private final AuthorConverter authorConverter;

    private final JpaGenreRepository jpaGenreRepository;

    private final GenreConverter genreConverter;

    private final JpaBookRepository jpaBookRepository;

    private final BookConverter bookConverter;

    private final JpaCommentRepository jpaCommentRepository;

    private final CommentConverter commentConverter;

    private final MongoJpaCache cache;

    private final JobOperator jobOperator;

    private final Job job;

    private final JobLauncher jobLauncher;

    @ShellMethod(value = "Find all authors", key = "aa")
    public String findAllAuthors() {
        var authors = jpaAuthorRepository.findAll();
        if (authors.isEmpty()) {
            return "No authors found";
        }
        return authors.stream()
                .map(authorConverter::authorToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find all books", key = "ab")
    public String findAllBooks() {
        var books = jpaBookRepository.findAll();
        if (books.isEmpty()) {
            return "No books found";
        }
        return books.stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find all comments", key = "ac")
    public String findAllCommentsByBookId() {
        var comments = jpaCommentRepository.findAll();
        if (comments.isEmpty()) {
            return "No comments found";
        }
        return comments.stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find all genres", key = "ag")
    public String findAllGenres() {
        var genres = jpaGenreRepository.findAll();
        if (genres.isEmpty()) {
            return "No genres found";
        }
        return genres.stream()
                .map(genreConverter::genreToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Start Migration Job With JobOperator", key = "sm-jo")
    public String startMigrationJobWithJobOperator() throws Exception {
        jobOperator.start(IMPORT_LIBRARY_JOB_NAME, new Properties());
        return "Done";
    }

    @ShellMethod(value = "Restart Migration Job With JobLauncher", key = "rm-jl")
    public String restartMigrationJobWithJobLauncher() throws Exception {
        jpaCommentRepository.deleteAll();
        jpaBookRepository.deleteAll();
        jpaAuthorRepository.deleteAll();
        jpaGenreRepository.deleteAll();

        cache.clear();

        jobLauncher.run(job, new JobParametersBuilder()
                        .addLong("trial", counter++)
                .toJobParameters());

        return "Done";
    }
}
