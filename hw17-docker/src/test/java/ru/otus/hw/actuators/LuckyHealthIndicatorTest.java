package ru.otus.hw.actuators;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;


@SpringBootTest(classes = {LuckyHealthIndicator.class})
class LuckyHealthIndicatorTest {

    @Autowired
    private LuckyHealthIndicator luckyHealthIndicator;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private BookRepository bookRepository;

    @Test
    void shouldAnyHealth() {
        Long author_id = 1L;
        Long book_id = 1L;

        Author author = new Author();
        author.setId(author_id);

        Book book = new Book();
        book.setId(book_id);
        book.setAuthor(author);

        doReturn(author_id).when(authorRepository).count();
        doReturn(book_id).when(bookRepository).count();
        doReturn(Optional.of(author)).when(authorRepository).findById(author_id);
        doReturn(Optional.of(book)).when(bookRepository).findById(book_id);

        Health health = luckyHealthIndicator.health();
        assertEquals(Status.UP, health.getStatus());
    }
}