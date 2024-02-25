package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Репозиторий для работы с книгами ")
@DataJpaTest
class JpaBookRepositoryTest {

    private static final long FIRST_BOOK_ID = 1L;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager em;

    private final List<Author> dbAuthors = new ArrayList<>();

    private final List<Genre> dbGenres = new ArrayList<>();

    private final List<Book> dbBooks = new ArrayList<>();

    @BeforeEach
    void setUp() {
        generateTestData();
    }

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        for (Book expectedBook : dbBooks) {
            Book actualBook = bookRepository.findById(expectedBook.getId()).orElse(new Book());
            assertThat(actualBook).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);
        }
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = bookRepository.findAll();
        var expectedBooks = dbBooks;

        assertThat(actualBooks).isNotNull().hasSize(expectedBooks.size());
        assertThat(actualBooks).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = new Book(0, "BookTitle_10500", dbAuthors.get(0), dbGenres.get(0));

        var returnedBook = bookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(em.find(Book.class, returnedBook.getId())).isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = new Book(FIRST_BOOK_ID, "BookTitle_10500", dbAuthors.get(2), dbGenres.get(2));

        assertThat(em.find(Book.class, expectedBook.getId())).isNotEqualTo(expectedBook);

        var returnedBook = bookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(em.find(Book.class, returnedBook.getId())).isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertNotNull(em.find(Book.class, FIRST_BOOK_ID));
        bookRepository.deleteById(FIRST_BOOK_ID);
        assertNull(em.find(Book.class, FIRST_BOOK_ID));
    }

    private void generateTestData() {
        for (int i = 1; i < 4; i++) {
            Author author = new Author(i, "Author_%s".formatted(i));
            Genre genre = new Genre(i, "Genre_%s".formatted(i));
            Book book = new Book(i, "BookTitle_%s".formatted(i), author, genre);

            dbAuthors.add(author);
            dbGenres.add(genre);
            dbBooks.add(book);
        }
    }
}