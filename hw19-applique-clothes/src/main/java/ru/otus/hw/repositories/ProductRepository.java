package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.db.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
