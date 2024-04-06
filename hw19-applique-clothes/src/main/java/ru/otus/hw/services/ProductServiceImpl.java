package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.ProductDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.DtoMapper;
import ru.otus.hw.models.db.Product;
import ru.otus.hw.repositories.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    private final DtoMapper dtoMapper;

    @Transactional(readOnly = true)
    @Override
    public ProductDto findById(long id) {
        return productRepository.findById(id)
                .map(dtoMapper::toProductDto)
                .orElseThrow(() -> new EntityNotFoundException("Product with id %d not found".formatted(id)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductDto> findAll() {
        return productRepository.findAll().stream()
                .map(dtoMapper::toProductDto)
                .toList();
    }

    @Transactional
    @Override
    public ProductDto insert(String fullName, String type, String size, BigDecimal price) {
        return dtoMapper.toProductDto(save(0, fullName, type, size, price));
    }


    @Transactional
    @Override
    public ProductDto insert(ProductDto product) {
        Product savedProduct = save(0, product.getFullName(), product.getType(), product.getSize(),
                product.getPrice());
        return dtoMapper.toProductDto(savedProduct);
    }

    @Transactional
    @Override
    public ProductDto update(long id, String fullName, String type, String size, BigDecimal price) {
        return dtoMapper.toProductDto(save(id, fullName, type, size, price));
    }

    @Transactional
    @Override
    public ProductDto update(ProductDto product) {
        Product savedProduct = save(product.getId(), product.getFullName(), product.getType(), product.getSize(),
                product.getPrice());
        return dtoMapper.toProductDto(savedProduct);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        productRepository.deleteById(id);
    }

    private Product save(long id, String fullName, String type, String size, BigDecimal price) {
        Product product = new Product(id, fullName, type, size, price);
        return productRepository.save(product);
    }
}
