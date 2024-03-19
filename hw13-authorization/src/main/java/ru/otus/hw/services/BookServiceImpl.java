package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.DtoMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final DtoMapper dtoMapper;

    @Transactional(readOnly = true)
    @Override
    public BookDto findById(long id) {
        return bookRepository.findById(id)
                .map(dtoMapper::toBookDto)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(dtoMapper::toBookDto)
                .toList();
    }

    @Transactional
    @Override
    public BookDto insert(String title, long authorId, long genreId) {
        return dtoMapper.toBookDto(save(0, title, authorId, genreId));
    }

    @Transactional
    @Override
    public BookDto insert(BookDto book) {
        return dtoMapper.toBookDto(save(0, book.getTitle(), book.getAuthor().getId(), book.getGenre().getId()));
    }

    @Transactional
    @Override
    public BookDto update(long id, String title, long authorId, long genreId) {
        return dtoMapper.toBookDto(save(id, title, authorId, genreId));
    }

    @Transactional
    @Override
    public BookDto update(BookDto book) {
        Book savedBook = save(book.getId(), book.getTitle(), book.getAuthor().getId(), book.getGenre().getId());
        return dtoMapper.toBookDto(savedBook);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
        commentRepository.deleteByBookId(id);
    }

    private Book save(long id, String title, long authorId, long genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(genreId)));
        var book = new Book(id, title, author, genre);
        return bookRepository.save(book);
    }
}
