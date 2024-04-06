package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.db.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(value = "order-entity-graph")
    @Override
    Optional<Order> findById(Long id);

    @EntityGraph(value = "order-entity-graph")
    @Override
    List<Order> findAll();
}
