package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.configurations.SecurityConfiguration;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.UserDetailsServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "user", authorities = "ROLE_USER")
@WebMvcTest({BookController.class, SecurityConfiguration.class})
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

    private final List<AuthorDto> dbAuthors = new ArrayList<>();

    private final List<GenreDto> dbGenres = new ArrayList<>();

    private final List<BookDto> dbBooks = new ArrayList<>();

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        generateTestData();
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnAllBooks() throws Exception {
        doReturn(dbBooks).when(bookService).findAll();

        mockMvc.perform(get("/api/v1/books"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dbBooks)));

        verify(bookService, times((1))).findAll();
    }

    @DisplayName("Анонимный пользователь. Ошибка при загрузке списка всех книг")
    @Test
    @WithAnonymousUser
    void shouldFailReturnAllBooks() throws Exception {
        doReturn(dbBooks).when(bookService).findAll();

        mockMvc.perform(get("/api/v1/books"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("должен загружать книгу")
    @Test
    void shouldReturnBookById() throws Exception {
        BookDto expectedBook = dbBooks.get(0);
        doReturn(expectedBook).when(bookService).findById(expectedBook.getId());

        mockMvc.perform(get("/api/v1/books/{id}", expectedBook.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedBook)));

        verify(bookService, times((1))).findById(expectedBook.getId());
    }

    @DisplayName("Анонимный пользователь. Ошибка при загрузке книги")
    @Test
    @WithAnonymousUser
    void shouldFailReturnBookById() throws Exception {
        BookDto expectedBook = dbBooks.get(0);
        doReturn(expectedBook).when(bookService).findById(expectedBook.getId());

        mockMvc.perform(get("/api/v1/books/{id}", expectedBook.getId()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() throws Exception {
        BookDto book = new BookDto(0, "BookTitle_New", dbAuthors.get(1), dbGenres.get(1));
        doReturn(book).when(bookService).insert(book);

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(book)));

        verify(bookService, times(1)).insert(book);
    }

    @DisplayName("Анонимный пользователь. Ошибка при сохранении новой книги")
    @Test
    @WithAnonymousUser
    void shouldFailSaveNewBook() throws Exception {
        BookDto book = new BookDto(0, "BookTitle_New", dbAuthors.get(1), dbGenres.get(1));
        doReturn(book).when(bookService).insert(book);

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() throws Exception {
        BookDto book = dbBooks.get(2);
        book.setTitle("BookTitle_Change");
        book.setAuthor(dbAuthors.get(0));
        book.setGenre(dbGenres.get(0));

        doReturn(book).when(bookService).update(book);

        mockMvc.perform(put("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(book)));

        verify(bookService, times(1)).update(book);
    }

    @DisplayName("Анонимный пользователь. Ошибка при сохранении измененной книги")
    @Test
    @WithAnonymousUser
    void shouldFailSaveUpdatedBook() throws Exception {
        BookDto book = dbBooks.get(2);
        book.setTitle("BookTitle_Change");
        book.setAuthor(dbAuthors.get(0));
        book.setGenre(dbGenres.get(0));

        doReturn(book).when(bookService).update(book);

        mockMvc.perform(put("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("должен удалять книгу по id")
    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void shouldDeleteBook() throws Exception {
        long firstBookId = 1L;

        mockMvc.perform(delete("/api/v1/books/{id}", firstBookId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookService, times((1))).deleteById(firstBookId);
    }

    @DisplayName("Анонимный пользователь. Ошибка при удалении книги по id")
    @Test
    @WithAnonymousUser
    void shouldFailDeleteBook() throws Exception {
        long firstBookId = 1L;

        mockMvc.perform(delete("/api/v1/books/{id}", firstBookId))
                .andDo(print())
                .andExpect(status().isUnauthorized());
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