package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;

import java.util.List;

public interface BookService {
    BookDto findById(long id);

    List<BookDto> findAll();

    BookDto insert(String title, long authorId, long genreId);

    BookDto insert(BookDto book);

    BookDto update(long id, String title, long authorId, long genreId);

    BookDto update(BookDto book);

    void deleteById(long id);
}
