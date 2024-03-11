package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.mappers.DtoMapper;
import ru.otus.hw.repositories.AuthorRepository;

@RequiredArgsConstructor
@RestController
public class AuthorController {
    private final AuthorRepository authorRepository;

    private final DtoMapper dtoMapper;

    @GetMapping("/api/v1/authors")
    public Flux<AuthorDto> getAllAuthors() {
        return authorRepository.findAll().map(dtoMapper::toAuthorDto);
    }
}