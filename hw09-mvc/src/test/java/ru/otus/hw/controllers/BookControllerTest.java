package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    private final List<AuthorDto> dbAuthors = new ArrayList<>();

    private final List<GenreDto> dbGenres = new ArrayList<>();

    private final List<BookDto> dbBooks = new ArrayList<>();

    @BeforeEach
    void setUp() {
        generateTestData();
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() throws Exception {
        BookDto book = new BookDto(0,"BookTitle_New", dbAuthors.get(1), dbGenres.get(1));

        mockMvc.perform(post("/book/edit").flashAttr("book", book))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(bookService, times(1)).update(book);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() throws Exception {
        BookDto book = dbBooks.get(2);
        book.setTitle("BookTitle_Change");
        book.setAuthor(dbAuthors.get(0));
        book.setGenre(dbGenres.get(0));

        mockMvc.perform(post("/book/edit").flashAttr("book", book))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(bookService, times(1)).update(book);
    }

    @DisplayName("должен удалять книгу по id")
    @Test
    void shouldDeleteBook() throws Exception {
        mockMvc.perform(delete("/book/delete").param("id", "1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        verify(bookService, times(1)).deleteById(1L);
    }

    private void generateTestData() {
        for (int i = 1; i < 4; i++) {
            AuthorDto author = new AuthorDto(i, "Author_%s".formatted(i));
            GenreDto genre = new GenreDto(i, "Genre_%s".formatted(i));
            BookDto book = new BookDto(i, "BookTitle_%s".formatted(i), author, genre);

            dbAuthors.add(author);
            dbGenres.add(genre);
            dbBooks.add(book);
        }
    }
}