package ru.otus.hw.services;

import ru.otus.hw.dto.ProductDto;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    ProductDto findById(long id);

    List<ProductDto> findAll();

    ProductDto insert(String fullName, String type, String size, BigDecimal price);

    ProductDto insert(ProductDto product);

    ProductDto update(long id, String fullName, String type, String size, BigDecimal price);

    ProductDto update(ProductDto product);

    void deleteById(long id);
}
