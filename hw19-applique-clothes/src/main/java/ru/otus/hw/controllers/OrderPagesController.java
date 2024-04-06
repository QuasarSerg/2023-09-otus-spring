package ru.otus.hw.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderPagesController {
    @GetMapping("/")
    public String ordersPage() {
        return "orders";
    }

    @GetMapping("/orders/edit")
    public String editOrderPage() {
        return "edit";
    }
}