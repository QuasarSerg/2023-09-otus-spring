package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "TestUser")
@WebMvcTest(GenreController.class)
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    @Autowired
    private ObjectMapper objectMapper;

    private final List<GenreDto> dbGenres = new ArrayList<>();


    @BeforeEach
    void setUp() {
        for (int i = 1; i < 4; i++) {
            dbGenres.add(new GenreDto(i, "Genre_%s".formatted(i)));
        }
    }

    @DisplayName("должен загружать все жанры")
    @Test
    void shouldReturnAllGenres() throws Exception {
        doReturn(dbGenres).when(genreService).findAll();

        mockMvc.perform(get("/api/v1/genres"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dbGenres)));

        verify(genreService, times(1)).findAll();
    }

    @DisplayName("Анонимный пользователь. Ошибка при загрузке всех жанров")
    @Test
    @WithAnonymousUser
    void shouldFailReturnAllGenres() throws Exception {
        doReturn(dbGenres).when(genreService).findAll();

        mockMvc.perform(get("/api/v1/genres"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}