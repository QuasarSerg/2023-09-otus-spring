package ru.otus.hw.service;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.models.Car;
import ru.otus.hw.models.SportCar;

import java.util.Collection;


@MessagingGateway
public interface SportCarGateway {
    @Gateway(requestChannel = "carChannel", replyChannel = "sportCarChannel")
    Collection<SportCar> process(Collection<Car> cars);
}