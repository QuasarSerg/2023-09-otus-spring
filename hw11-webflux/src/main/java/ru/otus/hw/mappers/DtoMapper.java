package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

@Mapper(componentModel = "spring")
public interface DtoMapper {
    AuthorDto toAuthorDto(Author author);

    Author toAuthor(AuthorDto author);

    GenreDto toGenreDto(Genre genre);

    Genre toGenre(GenreDto genre);

    BookDto toBookDto(Book book);

    Book toBook(BookDto book);

    CommentDto toCommentDto(Comment comment);

    Comment toComment(CommentDto comment);
}
