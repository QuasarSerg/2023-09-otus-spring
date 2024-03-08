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
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.mappers.DtoMapper;
import ru.otus.hw.mappers.DtoMapperImpl;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.CommentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@WebFluxTest
@ContextConfiguration(classes = {CommentController.class, DtoMapperImpl.class})
class CommentControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private DtoMapper dtoMapper;

    private Book book;

    private final List<Comment> dbComments = new ArrayList<>();

    private final List<CommentDto> dbCommentsDto = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Author author = new Author(UUID.randomUUID().toString(), "Author_%s".formatted(1));
        Genre genre = new Genre(UUID.randomUUID().toString(), "Genre_%s".formatted(1));
        book = new Book(UUID.randomUUID().toString(), "BookTitle_%s".formatted(1), author, genre);

        for (int i = 1; i < 4; i++) {
            Comment comment = new Comment(UUID.randomUUID().toString(), "Comment_%s".formatted(i), book);
            dbComments.add(comment);
            dbCommentsDto.add(dtoMapper.toCommentDto(comment));
        }
    }

    @DisplayName("должен загружать все комментарии для книги")
    @Test
    void shouldReturnAllCommentsByBookId() {
        String bookId = book.getId();

        doReturn(Flux.fromIterable(dbComments))
                .when(commentRepository)
                .findAllByBookId(bookId);

        webTestClient.get()
                .uri("/api/v1/books/{id}/comments", bookId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(CommentDto.class)
                .isEqualTo(dbCommentsDto);

        verify(commentRepository, times((1))).findAllByBookId(bookId);
    }
}