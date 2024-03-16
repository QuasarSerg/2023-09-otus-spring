package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mappers.DtoMapper;
import ru.otus.hw.mappers.DtoMapperImpl;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@WebFluxTest
@ContextConfiguration(classes = {BookController.class, DtoMapperImpl.class})
class BookControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private DtoMapper dtoMapper;

    private final List<Author> dbAuthors = new ArrayList<>();
    private final List<AuthorDto> dbAuthorsDto = new ArrayList<>();

    private final List<Genre> dbGenres = new ArrayList<>();
    private final List<GenreDto> dbGenresDto = new ArrayList<>();

    private final List<Book> dbBooks = new ArrayList<>();
    private final List<BookDto> dbBooksDto = new ArrayList<>();

    private final List<Comment> dbComments = new ArrayList<>();
    private final List<CommentDto> dbCommentsDto = new ArrayList<>();

    @BeforeEach
    void setUp() {
        generateTestData();
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnAllBooks() {
        doReturn(Flux.fromIterable(dbBooks))
                .when(bookRepository)
                .findAll();

        webTestClient.get()
                .uri("/api/v1/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(BookDto.class)
                .isEqualTo(dbBooksDto);

        verify(bookRepository, times(1)).findAll();
    }

    @DisplayName("должен загружать книгу")
    @Test
    void shouldReturnBookById() {
        Book expectedBook = dbBooks.get(0);
        BookDto expectedBookDto = dbBooksDto.get(0);
        doReturn(Mono.just(expectedBook)).when(bookRepository).findById(expectedBook.getId());

        webTestClient.get()
                .uri("/api/v1/books/{id}", expectedBook.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(BookDto.class)
                .isEqualTo(expectedBookDto);

        verify(bookRepository, times((1))).findById(expectedBook.getId());
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        Book book = new Book(null, "BookTitle_New", dbAuthors.get(1), dbGenres.get(1));
        BookDto bookDto = dtoMapper.toBookDto(book);
        doReturn(Mono.just(book.getAuthor())).when(authorRepository).findById(book.getAuthor().getId());
        doReturn(Mono.just(book.getGenre())).when(genreRepository).findById(book.getGenre().getId());
        doReturn(Mono.just(book)).when(bookRepository).save(any(Book.class));

        webTestClient.post()
                .uri("/api/v1/books")
                .body(BodyInserters.fromValue(bookDto))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(BookDto.class)
                .isEqualTo(bookDto);

        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        Book book = dbBooks.get(2);
        book.setTitle("BookTitle_Change");
        book.setAuthor(dbAuthors.get(0));
        book.setGenre(dbGenres.get(0));

        BookDto bookDto = dtoMapper.toBookDto(book);
        doReturn(Mono.just(book.getAuthor())).when(authorRepository).findById(book.getAuthor().getId());
        doReturn(Mono.just(book.getGenre())).when(genreRepository).findById(book.getGenre().getId());
        doReturn(Mono.just(book)).when(bookRepository).save(any(Book.class));

        webTestClient.put()
                .uri("/api/v1/books")
                .body(BodyInserters.fromValue(bookDto))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(BookDto.class)
                .isEqualTo(bookDto);

        verify(authorRepository, times(1)).findById(book.getAuthor().getId());
        verify(genreRepository, times(1)).findById(book.getGenre().getId());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @DisplayName("должен удалять книгу по id")
    @Test
    void shouldDeleteBook() {
        String bookId = dbBooks.get(2).getId();
        doReturn(Mono.empty()).when(bookRepository).deleteById(bookId);
        doReturn(Mono.empty()).when(commentRepository).deleteAllByBookId(bookId);

        webTestClient.delete()
                .uri("/api/v1/books/{id}", bookId)
                .exchange()
                .expectStatus()
                .isOk();

        verify(bookRepository, times((1))).deleteById(bookId);
        verify(commentRepository, times((1))).deleteAllByBookId(bookId);
    }

    private void generateTestData() {
        for (int i = 1; i < 4; i++) {
            Author author = new Author(UUID.randomUUID().toString(), "Author_%s".formatted(i));
            Genre genre = new Genre(UUID.randomUUID().toString(), "Genre_%s".formatted(i));
            Book book = new Book(UUID.randomUUID().toString(), "BookTitle_%s".formatted(i), author, genre);

            dbAuthors.add(author);
            dbAuthorsDto.add(dtoMapper.toAuthorDto(author));
            dbGenres.add(genre);
            dbGenresDto.add(dtoMapper.toGenreDto(genre));
            dbBooks.add(book);
            dbBooksDto.add(dtoMapper.toBookDto(book));

            for (int j = 1; j < 4; j++) {
                Comment comment = new Comment(UUID.randomUUID().toString(), "Comment_%s".formatted(j), book);
                dbComments.add(comment);
                dbCommentsDto.add(dtoMapper.toCommentDto(comment));
            }
        }
    }
}