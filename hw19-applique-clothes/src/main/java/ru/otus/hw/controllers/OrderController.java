package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.hw.dto.OrderDto;
import ru.otus.hw.services.OrderService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/api/v1/orders")
    public List<OrderDto> getAllOrders() {
        return orderService.findAll();
    }

    @GetMapping("/api/v1/orders/{id}")
    public OrderDto getOrder(@PathVariable("id") long id) {
        return orderService.findById(id);
    }

    @PostMapping("/api/v1/orders")
    public OrderDto addOrder(@RequestBody @Valid OrderDto order) {
        return orderService.insert(order);
    }

    @PutMapping("/api/v1/orders")
    public OrderDto updateOrder(@RequestBody @Valid OrderDto order) {
        return orderService.update(order);
    }

    @DeleteMapping("/api/v1/orders/{id}")
    public void deleteOrder(@PathVariable("id") long id) {
        orderService.deleteById(id);
    }
}