package ru.otus.hw.actuators;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.BookRepository;

@Component
public class BooksIndicator  implements HealthIndicator {

    private final BookRepository bookRepository;

    public BooksIndicator(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Health health() {
        var books = bookRepository.findAll();
        if (books.isEmpty()) {
            return Health.down().withDetail("No books found", "No books found").build();
        }
        return Health.up().withDetail("message", "Have books").build();
    }
}
