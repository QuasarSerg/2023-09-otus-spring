package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDto> findById(long id);

    List<BookDto> findAll();

    Book insert(String title, long authorId, long genreId);

    Book update(long id, String title, long authorId, long genreId);

    Book update(BookDto book);

    void deleteById(long id);
}
