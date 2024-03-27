package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.feign.BooksService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BooksService booksService;

    @Transactional(readOnly = true)
    @Override
    public BookDto findById(long id) {
        return booksService.getBook(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return booksService.listBooks();
    }

    @Transactional
    @Override
    public BookDto insert(BookDto book) {
        return booksService.addBook(book);
    }

    @Transactional
    @Override
    public BookDto update(BookDto book) {
        return booksService.updateBook(book);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        booksService.deleteBook(id);
    }
}
