package ru.otus.hw.feign;

import ru.otus.hw.dto.BookDto;

import java.util.List;

public class BooksServiceImpl implements BooksService {

    @Override
    public List<BookDto> listBooks() {
        return List.of(new BookDto(1L, "Book 1", null, null));
    }

    @Override
    public BookDto getBook(long id) {
        return new BookDto(1L, "Book 1", null, null);
    }

    @Override
    public BookDto addBook(BookDto book) {
        return new BookDto(1L, "Book 1", null, null);
    }

    @Override
    public BookDto updateBook(BookDto book) {
        return new BookDto(1L, "Book 1", null, null);
    }

    @Override
    public void deleteBook(long id) {

    }
}
