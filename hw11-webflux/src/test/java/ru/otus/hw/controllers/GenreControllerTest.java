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
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mappers.DtoMapper;
import ru.otus.hw.mappers.DtoMapperImpl;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@WebFluxTest
@ContextConfiguration(classes = {GenreController.class, DtoMapperImpl.class})
class GenreControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private DtoMapper dtoMapper;

    private final List<Genre> dbGenres = new ArrayList<>();
    private final List<GenreDto> dbGenresDto = new ArrayList<>();

    @BeforeEach
    void setUp() {
        for (int i = 1; i < 4; i++) {
            Genre genre = new Genre(String.valueOf(i), "Genre_%s".formatted(i));
            dbGenres.add(genre);
            dbGenresDto.add(dtoMapper.toGenreDto(genre));
        }
    }

    @DisplayName("должен загружать все жанры")
    @Test
    void shouldReturnAllAuthors() {
        doReturn(Flux.fromIterable(dbGenres))
                .when(genreRepository)
                .findAll();

        webTestClient.get()
                .uri("/api/v1/genres")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(GenreDto.class)
                .isEqualTo(dbGenresDto);

        verify(genreRepository, times(1)).findAll();
    }
}