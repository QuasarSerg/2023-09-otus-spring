package ru.otus.hw.actuators;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
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

        Long bookId = secureRandom.nextLong(authorRepository.count()) + 1;
        Long authorId = secureRandom.nextLong(bookRepository.count()) + 1;

        if (bookRepository.existsByIdAndAuthorId(bookId, authorId)) {
            return Health.up().status(Status.UP)
                    .withDetail("message", "Все в порядке!").build();
        } else {
            return Health.down().status(Status.DOWN)
                    .withDetail("message", "Не повезло, попробуйте еще раз.").build();
        }
    }
}
