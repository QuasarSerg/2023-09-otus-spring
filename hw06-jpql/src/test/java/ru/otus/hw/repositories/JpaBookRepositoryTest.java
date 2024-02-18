package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
@Import({JpaAuthorRepository.class, JpaGenreRepository.class, JpaBookRepository.class})
class JpaBookRepositoryTest {

    private static final long FIRST_BOOK_ID = 1L;

    @Autowired
    private JpaAuthorRepository jpaAuthorRepository;

    @Autowired
    private JpaGenreRepository jpaGenreRepository;

    @Autowired
    private JpaBookRepository jpaBookRepository;

    @Autowired
    private TestEntityManager em;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = jpaAuthorRepository.findAll();
        dbGenres = jpaGenreRepository.findAll();
        dbBooks = jpaBookRepository.findAll();
    }

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        for (Book expectedBook : dbBooks) {
            Book actualBook = jpaBookRepository.findById(expectedBook.getId()).orElse(new Book());
            assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
        }
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = jpaBookRepository.findAll();
        var expectedBooks = dbBooks;

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        assertThat(actualBooks).usingRecursiveComparison().isEqualTo(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = new Book(0, "BookTitle_10500", dbAuthors.get(0), dbGenres.get(0));

        var returnedBook = jpaBookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(em.getEntityManager().find(Book.class, returnedBook.getId())).isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = new Book(FIRST_BOOK_ID, "BookTitle_10500", dbAuthors.get(2), dbGenres.get(2));

        assertThat(em.getEntityManager().find(Book.class, expectedBook.getId())).isNotEqualTo(expectedBook);

        var returnedBook = jpaBookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(em.getEntityManager().find(Book.class, returnedBook.getId())).isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertNotNull(em.getEntityManager().find(Book.class, FIRST_BOOK_ID));
        jpaBookRepository.deleteById(FIRST_BOOK_ID);
        assertNull(em.getEntityManager().find(Book.class, FIRST_BOOK_ID));
    }
}