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
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.mappers.DtoMapper;
import ru.otus.hw.mappers.DtoMapperImpl;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@WebFluxTest
@ContextConfiguration(classes = {AuthorController.class, DtoMapperImpl.class})
class AuthorControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthorRepository authorRepository;

    @Autowired
    private DtoMapper dtoMapper;

    private final List<Author> dbAuthors = new ArrayList<>();
    private final List<AuthorDto> dbAuthorsDto = new ArrayList<>();

    @BeforeEach
    void setUp() {
        for (int i = 1; i < 4; i++) {
            Author author = new Author(String.valueOf(i), "Author_%s".formatted(i));
            dbAuthors.add(author);
            dbAuthorsDto.add(dtoMapper.toAuthorDto(author));
        }
    }

    @DisplayName("должен загружать всех авторов")
    @Test
    void shouldReturnAllAuthors() {
        doReturn(Flux.fromIterable(dbAuthors))
                .when(authorRepository)
                .findAll();

        webTestClient.get()
                .uri("/api/v1/authors")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(AuthorDto.class)
                .isEqualTo(dbAuthorsDto);

        verify(authorRepository, times(1)).findAll();
    }
}