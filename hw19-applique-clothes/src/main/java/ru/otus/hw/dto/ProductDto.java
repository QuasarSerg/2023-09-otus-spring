package ru.otus.hw.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private long id;

    @NotNull
    private String fullName;

    @NotNull
    private String type;

    @NotNull
    private String size;

    @NotNull
    private BigDecimal price;
}
