package ru.otus.hw.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.otus.hw.dto.BookDto;

import java.util.List;

@FeignClient(name = "books-service")
public interface BooksService {

    @CircuitBreaker(name = "getBooks")
    @GetMapping("/api/v1/books")
    public List<BookDto> listBooks();

    @CircuitBreaker(name = "getBook")
    @GetMapping("/api/v1/books/{id}")
    public BookDto getBook(@PathVariable("id") long id);

    @CircuitBreaker(name = "saveBook")
    @PostMapping("/api/v1/books")
    public BookDto addBook(@RequestBody @Valid BookDto book);

    @CircuitBreaker(name = "updateBook")
    @PutMapping("/api/v1/books")
    public BookDto updateBook(@RequestBody @Valid BookDto book);

    @DeleteMapping("/api/v1/books/{id}")
    public void deleteBook (@PathVariable("id") long id);

}