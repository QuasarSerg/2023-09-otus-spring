package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mappers.DtoMapper;
import ru.otus.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@RestController
public class GenreController {
    private final GenreRepository genreRepository;

    private final DtoMapper dtoMapper;

    @GetMapping("/api/v1/genres")
    public Flux<GenreDto> getAllGenres() {
        return genreRepository.findAll().map(dtoMapper::toGenreDto);
    }
}