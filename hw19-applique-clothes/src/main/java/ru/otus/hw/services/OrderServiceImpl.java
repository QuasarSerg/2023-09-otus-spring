package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.OrderDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.DtoMapper;
import ru.otus.hw.models.db.Order;
import ru.otus.hw.models.db.Product;
import ru.otus.hw.repositories.OrderRepository;
import ru.otus.hw.repositories.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final DtoMapper dtoMapper;

    @Transactional(readOnly = true)
    @Override
    public List<OrderDto> findAll() {
        return orderRepository.findAll().stream().map(dtoMapper::toOrderDto).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public OrderDto findById(long id) {
        return orderRepository.findById(id)
                .map(dtoMapper::toOrderDto)
                .orElseThrow(() -> new EntityNotFoundException("Order with id %d not found".formatted(id)));
    }

    @Transactional
    @Override
    public OrderDto insert(long productId, Integer amount, BigDecimal price, String applique) {
        return dtoMapper.toOrderDto(save(0, productId, amount, price, applique));
    }

    @Transactional
    @Override
    public OrderDto insert(OrderDto order) {
        Order savedOrder = save(0, order.getProduct().getId(), order.getAmount(), order.getPrice(),
                order.getApplique());
        return dtoMapper.toOrderDto(savedOrder);
    }

    @Transactional
    @Override
    public OrderDto update(long id, long productId, Integer amount, BigDecimal price, String applique) {
        return dtoMapper.toOrderDto(save(id, productId, amount, price, applique));
    }

    @Transactional
    @Override
    public OrderDto update(OrderDto order) {
        Order savedOrder = save(order.getId(), order.getProduct().getId(), order.getAmount(), order.getPrice(),
                order.getApplique());
        return dtoMapper.toOrderDto(savedOrder);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        orderRepository.deleteById(id);
    }

    private Order save(long orderId, long productId, Integer amount, BigDecimal price, String applique) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product with id %d not found".formatted(productId)));
        Order order = new Order(orderId, product, amount, price, applique);
        return orderRepository.save(order);
    }
}
