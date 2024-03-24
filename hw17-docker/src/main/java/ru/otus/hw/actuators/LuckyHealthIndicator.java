package ru.otus.hw.actuators;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class LuckyHealthIndicator implements HealthIndicator {

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    @Override
    public Health health() {
        SecureRandom secureRandom = new SecureRandom();

        Author author = authorRepository.findById(secureRandom.nextLong(authorRepository.count()) + 1)
                .orElse(new Author());
        Book book = bookRepository.findById(secureRandom.nextLong(bookRepository.count()) + 1)
                .orElse(new Book());

        if (book.getAuthor().equals(author)) {
            return Health.up().status(Status.UP)
                    .withDetail("message", "Все в порядке!").build();
        } else {
            return Health.down().status(Status.DOWN)
                    .withDetail("message", "Не повезло, попробуйте еще раз.").build();
        }
    }
}
