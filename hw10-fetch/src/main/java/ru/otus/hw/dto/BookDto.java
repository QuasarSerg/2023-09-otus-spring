package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    @NotNull
    private long id;

    @NotBlank(message = "{title-field-must-not-be-blank}")
    private String title;

    @NotNull
    private AuthorDto author;

    @NotNull
    private GenreDto genre;
}
