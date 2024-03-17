package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.DtoMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;


@Slf4j
@RequiredArgsConstructor
@RestController
public class BookController {
    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final CommentRepository commentRepository;

    private final DtoMapper dtoMapper;

    @GetMapping("/api/v1/books")
    public Flux<BookDto> listBooks() {
        return bookRepository.findAll().map(dtoMapper::toBookDto);
    }

    @GetMapping("/api/v1/books/{id}")
    public Mono<BookDto> getBook(@PathVariable("id") String id) {
        return bookRepository.findById(id)
                .map(dtoMapper::toBookDto)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book with id %s not found".formatted(id))));
    }

    @PostMapping("/api/v1/books")
    public Mono<BookDto> addBook(@RequestBody @Valid BookDto book) {
        return save(null, book.getTitle(), book.getAuthor().getId(), book.getGenre().getId());
    }

    @PutMapping("/api/v1/books")
    public Mono<BookDto> updateBook(@RequestBody @Valid BookDto book) {
        return save(book.getId(), book.getTitle(), book.getAuthor().getId(), book.getGenre().getId());
    }

    @DeleteMapping("/api/v1/books/{id}")
    public Mono<Void> deleteBook(@PathVariable("id") String id) {
        return bookRepository.deleteById(id).then(commentRepository.deleteAllByBookId(id));
    }

    private Mono<BookDto> save(String id, String title, String authorId, String genreId) {
        return authorRepository.findById(authorId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Author with id %s not found"
                        .formatted(authorId))))
                .zipWith(genreRepository.findById(genreId))
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Genre with id %s not found".formatted(genreId))))
                .flatMap(tuple2 -> bookRepository.save(new Book(id, title, tuple2.getT1(), tuple2.getT2()))
                        .map(dtoMapper::toBookDto));
    }
}