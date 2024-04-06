package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.hw.dto.ProductDto;
import ru.otus.hw.services.ProductService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProductController {
    private final ProductService productService;

    @GetMapping("/api/v1/products")
    public List<ProductDto> listOrders() {
        return productService.findAll();
    }

    @GetMapping("/api/v1/products/{id}")
    public ProductDto getOrder(@PathVariable("id") long id) {
        return productService.findById(id);
    }

    @PostMapping("/api/v1/products")
    public ProductDto addOrder(@RequestBody @Valid ProductDto product) {
        return productService.insert(product);
    }

    @PutMapping("/api/v1/products")
    public ProductDto updateOrder(@RequestBody @Valid ProductDto product) {
        return productService.update(product);
    }

    @DeleteMapping("/api/v1/products/{id}")
    public void deleteOrder(@PathVariable("id") long id) {
        productService.deleteById(id);
    }

}