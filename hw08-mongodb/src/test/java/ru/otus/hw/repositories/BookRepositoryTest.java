package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Репозиторий для работы с книгами ")
@DataMongoTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

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
        for (int i = 0; i < 2; i++) {
            Book expectedBook = dbBooks.get(i);
            Book actualBook = bookRepository.findById(expectedBook.getId()).orElse(new Book());
            assertThat(actualBook).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);
        }
    }

    @DisplayName("должен загружать список всех книг по списку id")
    @Test
    void shouldReturnCorrectBooksList() {
        List<Book> actualBooks = bookRepository.findAllById(List.of(dbBooks.get(0).getId(), dbBooks.get(1).getId()));
        List<Book> expectedBooks = List.of(dbBooks.get(0), dbBooks.get(1));

        assertThat(actualBooks).isNotNull().hasSize(expectedBooks.size());
        assertThat(actualBooks).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        Book expectedBook = new Book(null, "BookTitle_New", dbAuthors.get(0), dbGenres.get(0));
        Book returnedBook = bookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> !book.getId().isEmpty())
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        Book expectedBook = dbBooks.get(2);
        expectedBook.setTitle("BookTitle_Change");
        expectedBook.setAuthor(dbAuthors.get(0));
        expectedBook.setGenre(dbGenres.get(0));

        Query queryBook = new Query();
        queryBook.addCriteria(Criteria.where("id").is(expectedBook.getId()));

        assertThat(mongoTemplate.findOne(queryBook, Book.class)).isNotEqualTo(expectedBook);

        var returnedBook = bookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(b -> !b.getId().isEmpty())
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        String firstBookId = dbBooks.get(2).getId();
        Query queryBook = new Query();
        queryBook.addCriteria(Criteria.where("id").is(firstBookId));

        assertNotNull(mongoTemplate.findOne(queryBook, Book.class));
        bookRepository.deleteById(firstBookId);
        assertNull(mongoTemplate.findOne(queryBook, Book.class));
    }

    private void generateTestData() {
        List<Author> authorList = mongoTemplate.findAll(Author.class);
        List<Genre> genreList = mongoTemplate.findAll(Genre.class);
        List<Book> bookList = mongoTemplate.findAll(Book.class);

        for (int i = 1; i < 4; i++) {
            Author author = new Author(authorList.get(i - 1).getId(), "Author_%s".formatted(i));
            Genre genre = new Genre(genreList.get(i - 1).getId(), "Genre_%s".formatted(i));
            Book book = new Book(bookList.get(i - 1).getId(), "BookTitle_%s".formatted(i), author, genre);

            dbAuthors.add(author);
            dbGenres.add(genre);
            dbBooks.add(book);
        }
    }
}