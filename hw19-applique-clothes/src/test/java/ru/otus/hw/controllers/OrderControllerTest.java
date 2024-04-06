package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.configurations.SecurityConfiguration;
import ru.otus.hw.dto.OrderDto;
import ru.otus.hw.dto.ProductDto;
import ru.otus.hw.services.OrderService;
import ru.otus.hw.services.UserDetailsServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "user", authorities = "ROLE_USER")
@WebMvcTest({OrderController.class, SecurityConfiguration.class})
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

    private final List<ProductDto> dbProducts = new ArrayList<>();

    private final List<OrderDto> dbOrders = new ArrayList<>();

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        generateTestData();
    }

    @DisplayName("должен загружать список всех заказов")
    @Test
    void shouldReturnAllOrders() throws Exception {
        doReturn(dbOrders).when(orderService).findAll();

        mockMvc.perform(get("/api/v1/orders"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dbOrders)));

        verify(orderService, times((1))).findAll();
    }

    @DisplayName("Анонимный пользователь. Ошибка при загрузке списка всех заказов")
    @Test
    @WithAnonymousUser
    void shouldFailReturnAllOrders() throws Exception {
        doReturn(dbOrders).when(orderService).findAll();

        mockMvc.perform(get("/api/v1/orders"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("должен загружать заказ")
    @Test
    void shouldReturnOrderById() throws Exception {
        OrderDto expectedOrder = dbOrders.get(0);
        doReturn(expectedOrder).when(orderService).findById(expectedOrder.getId());

        mockMvc.perform(get("/api/v1/orders/{id}", expectedOrder.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedOrder)));

        verify(orderService, times((1))).findById(expectedOrder.getId());
    }

    @DisplayName("Анонимный пользователь. Ошибка при загрузке заказа")
    @Test
    @WithAnonymousUser
    void shouldFailReturnOrderById() throws Exception {
        OrderDto expectedOrder = dbOrders.get(0);
        doReturn(expectedOrder).when(orderService).findById(expectedOrder.getId());

        mockMvc.perform(get("/api/v1/orders/{id}", expectedOrder.getId()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("должен сохранять новый заказ")
    @Test
    void shouldSaveNewOrder() throws Exception {
        OrderDto order = new OrderDto(0, dbProducts.get(0), 1, BigDecimal.TEN, "TestApplique1");
        doReturn(order).when(orderService).insert(order);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(order)));

        verify(orderService, times(1)).insert(order);
    }

    @DisplayName("Анонимный пользователь. Ошибка при сохранении нового заказа")
    @Test
    @WithAnonymousUser
    void shouldFailSaveNewOrder() throws Exception {
        OrderDto order = new OrderDto(0, dbProducts.get(0), 1, BigDecimal.TEN, "TestApplique1");
        doReturn(order).when(orderService).insert(order);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("должен сохранять измененный заказ")
    @Test
    void shouldSaveUpdatedOrder() throws Exception {
        OrderDto order = dbOrders.get(2);
        order.setAmount(15);
        order.setProduct(dbProducts.get(0));

        doReturn(order).when(orderService).update(order);

        mockMvc.perform(put("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(order)));

        verify(orderService, times(1)).update(order);
    }

    @DisplayName("Анонимный пользователь. Ошибка при сохранении измененного заказа")
    @Test
    @WithAnonymousUser
    void shouldFailSaveUpdatedOrder() throws Exception {
        OrderDto order = dbOrders.get(2);
        order.setAmount(15);
        order.setProduct(dbProducts.get(0));

        doReturn(order).when(orderService).update(order);

        mockMvc.perform(put("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("должен удалять заказ по id")
    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void shouldDeleteOrder() throws Exception {
        long firstOrderId = 1L;

        mockMvc.perform(delete("/api/v1/orders/{id}", firstOrderId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(orderService, times((1))).deleteById(firstOrderId);
    }

    @DisplayName("Анонимный пользователь. Ошибка при удалении заказа по id")
    @Test
    @WithAnonymousUser
    void shouldFailDeleteOrder() throws Exception {
        long firstOrderId = 1L;

        mockMvc.perform(delete("/api/v1/orders/{id}", firstOrderId))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    private void generateTestData() {
        for (int i = 1; i < 4; i++) {
            ProductDto product = new ProductDto(i, "Product_%s".formatted(i), "Футболка", "XL", BigDecimal.TEN);
            OrderDto order = new OrderDto(i, product, 5, BigDecimal.TEN, "Applique_%s".formatted(i));

            dbProducts.add(product);
            dbOrders.add(order);
        }
    }
}