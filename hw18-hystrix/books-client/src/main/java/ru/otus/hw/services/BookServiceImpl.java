package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.feign.BooksServiceProxy;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BooksServiceProxy booksServiceProxy;

    @Transactional(readOnly = true)
    @Override
    public BookDto findById(long id) {
        return booksServiceProxy.getBook(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return booksServiceProxy.listBooks();
    }

    @Transactional
    @Override
    public BookDto insert(BookDto book) {
        return booksServiceProxy.addBook(book);
    }

    @Transactional
    @Override
    public BookDto update(BookDto book) {
        return booksServiceProxy.updateBook(book);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        booksServiceProxy.deleteBook(id);
    }
}
