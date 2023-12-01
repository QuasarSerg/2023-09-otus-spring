package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final JdbcOperations jdbc;

    private final NamedParameterJdbcOperations namedParameterJdbc;

    @Override
    public Optional<Book> findById(long id) {
        String request = "select \n" +
                "  b.id, \n" +
                "  b.title, \n" +
                "  b.author_id, \n" +
                "  a.full_name as author_full_name, \n" +
                "  b.genre_id, \n" +
                "  g.name as genre_name \n" +
                "from \n" +
                "  books as b \n" +
                "  left join authors as a on b.author_id = a.id \n" +
                "  left join genres as g on b.genre_id = g.id \n" +
                "where \n" +
                "  b.id = :id";
        Map<String, Object> params = Collections.singletonMap("id", id);
        List<Book> bookList = namedParameterJdbc.query(request, params, new BookRowMapper());
        return bookList.stream().findFirst();
    }

    @Override
    public List<Book> findAll() {
        String request = "select \n" +
                "  b.id, \n" +
                "  b.title, \n" +
                "  b.author_id, \n" +
                "  a.full_name as author_full_name, \n" +
                "  b.genre_id, \n" +
                "  g.name as genre_name \n" +
                "from \n" +
                "  books as b \n" +
                "  left join authors as a on b.author_id = a.id \n" +
                "  left join genres as g on b.genre_id = g.id\n";
        return jdbc.query(request, new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        namedParameterJdbc.update("delete from books where id = :id", params);
    }

    private Book insert(Book book) {

        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("title", book.getTitle());
        paramSource.addValue("author_id", book.getAuthor().getId());
        paramSource.addValue("genre_id", book.getGenre().getId());

        namedParameterJdbc.update("insert into books (title, author_id, genre_id)" +
                        " values (:title, :author_id, :genre_id)", paramSource, keyHolder);

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", book.getId());
        paramSource.addValue("title", book.getTitle());
        paramSource.addValue("author_id", book.getAuthor().getId());
        paramSource.addValue("genre_id", book.getGenre().getId());

        int rowsCount = namedParameterJdbc.update(
                "update books set title = :title, author_id = :author_id, genre_id = :genre_id where id = :id",
                paramSource, keyHolder);

        if (rowsCount == 0) {
            throw new EntityNotFoundException("Book with id %d not found".formatted(book.getId()));
        }
        // Выбросить EntityNotFoundException если не обновлено ни одной записи в БД
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String title = rs.getString("title");
            long authorId = rs.getLong("author_id");
            String authorFullName = rs.getString("author_full_name");
            long genreId = rs.getLong("genre_id");
            String genreName = rs.getString("genre_name");

            return new Book(id, title, new Author(authorId, authorFullName), new Genre(genreId, genreName));
        }
    }
}
