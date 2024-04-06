package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.OrderDto;
import ru.otus.hw.dto.ProductDto;
import ru.otus.hw.models.db.Order;
import ru.otus.hw.models.db.Product;

@Mapper(componentModel = "spring")
public interface DtoMapper {
    OrderDto toOrderDto(Order order);

    ProductDto toProductDto(Product product);
}
