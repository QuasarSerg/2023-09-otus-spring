package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.db.Product;
import ru.otus.hw.models.db.Order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Репозиторий для работы с заказами")
@DataJpaTest
class JpaOrderRepositoryTest {

    private static final long FIRST_ORDER_ID = 1L;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TestEntityManager em;

    private final List<Product> dbProducts = new ArrayList<>();

    private final List<Order> dbOrders = new ArrayList<>();

    @BeforeEach
    void setUp() {
        generateTestData();
    }

//    @DisplayName("должен возвращать заказ по id")
//    @Test
//    void shouldReturnCorrectOrderById() {
//        for (Order expectedOrder : dbOrders) {
//            Order actualOrder = orderRepository.findById(expectedOrder.getId()).orElse(new Order());
//            assertThat(actualOrder).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedOrder);
//        }
//    }

    @DisplayName("должен загружать список всех заказов")
    @Test
    void shouldReturnCorrectOrdersList() {
        var actualOrders = orderRepository.findAll();
        var expectedOrders = dbOrders;

//        assertThat(actualOrders).isNotNull().hasSize(expectedOrders.size());
//        assertThat(actualOrders).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedOrders);
        actualOrders.forEach(System.out::println);
    }

//    @DisplayName("должен сохранять новый заказ")
//    @Test
//    void shouldSaveNewOrder() {
//        var expectedOrder = new Order(0, dbProducts.get(0), 1, BigDecimal.TEN, "TestApplique1");
//        var returnedOrder = orderRepository.save(expectedOrder);
//        assertThat(returnedOrder).isNotNull()
//                .matches(order -> order.getId() > 0)
//                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedOrder);
//
//        assertThat(em.find(Order.class, returnedOrder.getId())).isEqualTo(returnedOrder);
//    }
//
//    @DisplayName("должен сохранять измененный заказ")
//    @Test
//    void shouldSaveUpdatedOrder() {
//        var expectedOrder = new Order(FIRST_ORDER_ID, dbProducts.get(2), 2, BigDecimal.TEN, "TestApplique2");
//
//        assertThat(em.find(Order.class, expectedOrder.getId())).isNotEqualTo(expectedOrder);
//
//        var returnedOrder = orderRepository.save(expectedOrder);
//        assertThat(returnedOrder).isNotNull()
//                .matches(order -> order.getId() > 0)
//                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedOrder);
//
//        assertThat(em.find(Order.class, returnedOrder.getId())).isEqualTo(returnedOrder);
//    }
//
//    @DisplayName("должен удалять заказ по id")
//    @Test
//    void shouldDeleteOrder() {
//        assertNotNull(em.find(Order.class, FIRST_ORDER_ID));
//        orderRepository.deleteById(FIRST_ORDER_ID);
//        assertNull(em.find(Order.class, FIRST_ORDER_ID));
//    }

    private void generateTestData() {
        for (int i = 1; i < 4; i++) {
            Product product = new Product(i, "Product_%s".formatted(i), "Футболка", "XL", BigDecimal.TEN);
            Order order = new Order(i, product, 5, BigDecimal.TEN, "Applique_%s".formatted(i));

            dbProducts.add(product);
            dbOrders.add(order);
        }
    }
}