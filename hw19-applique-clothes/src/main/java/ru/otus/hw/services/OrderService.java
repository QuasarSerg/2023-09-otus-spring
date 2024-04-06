package ru.otus.hw.services;

import ru.otus.hw.dto.OrderDto;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    List<OrderDto> findAll();

    OrderDto findById(long id);

    OrderDto insert(long productId, Integer amount, BigDecimal price, String applique);

    OrderDto insert(OrderDto order);

    OrderDto update(long id, long productId, Integer amount, BigDecimal price, String applique);

    OrderDto update(OrderDto order);

    void deleteById(long id);
}
