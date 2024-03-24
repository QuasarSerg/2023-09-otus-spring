package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import ru.otus.hw.models.mongo.AuthorDocument;
import ru.otus.hw.models.mongo.BookDocument;
import ru.otus.hw.models.mongo.CommentDocument;
import ru.otus.hw.models.mongo.GenreDocument;
import ru.otus.hw.models.sql.Author;
import ru.otus.hw.models.sql.Book;
import ru.otus.hw.models.sql.Comment;
import ru.otus.hw.models.sql.Genre;

@Mapper(componentModel = "spring")
public interface MigrationMapper {
    AuthorDocument toAuthorDocument(Author author);

    GenreDocument toGenreDocument(Genre genre);

    BookDocument toBookDocument(Book book);

    CommentDocument toCommentDocument(Comment comment);
}
