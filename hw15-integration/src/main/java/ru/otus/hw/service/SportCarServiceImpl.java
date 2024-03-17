package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Car;
import ru.otus.hw.models.SportCar;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SportCarServiceImpl implements SportCarService {

    private final List<String> modifications = List.of("Двигатель", "Тормоза", "Каркас безопасности");

    private final SportCarGateway sportCarGateway;

    @Override
    public SportCar transform(Car car) {
        log.info("Upgrade car {}", car.getName());
        SecureRandom secureRandom = new SecureRandom();
        int indexMod = secureRandom.nextInt(modifications.size());
        SportCar sportCar = new SportCar(car.getName(), modifications.get(indexMod));
        delay();
        log.info("Upgrade car {} done", car.getName());

        return sportCar;
    }

    private static void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void runProcess() {
        var caterpillars = List.of(
                new Car("Aurus"),
                new Car("GAZ"),
                new Car("Lada"),
                new Car("Moskvich"),
                new Car("KamAZ"),
                new Car("UAZ"),
                new Car("Sollers")
        );
        Collection<SportCar> result = sportCarGateway.process(caterpillars);
        log.info(result.toString());
    }
}
