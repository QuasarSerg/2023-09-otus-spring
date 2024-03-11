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
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "TestUser")
@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Autowired
    private ObjectMapper objectMapper;

    private final List<AuthorDto> dbAuthors = new ArrayList<>();

    @BeforeEach
    void setUp() {
        for (int i = 1; i < 4; i++) {
            dbAuthors.add(new AuthorDto(i, "Author_%s".formatted(i)));
        }
    }

    @DisplayName("должен загружать всех авторов")
    @Test
    void shouldReturnAllAuthors() throws Exception {
        doReturn(dbAuthors).when(authorService).findAll();

        mockMvc.perform(get("/api/v1/authors"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(dbAuthors)));

        verify(authorService, times(1)).findAll();
    }

    @DisplayName("Анонимный пользователь. Ошибка при получении всех авторов")
    @Test
    @WithAnonymousUser
    void shouldFailReturnAllAuthors() throws Exception {
        doReturn(dbAuthors).when(authorService).findAll();

        mockMvc.perform(get("/api/v1/authors"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}