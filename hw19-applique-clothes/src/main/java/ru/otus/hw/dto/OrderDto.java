package ru.otus.hw.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private long id;

    @NotNull
    private ProductDto product;

    @NotNull
    private Integer amount;

    @NotNull
    private BigDecimal price;

    @NotNull(message = "Applique should be filled")
    private String applique;
}