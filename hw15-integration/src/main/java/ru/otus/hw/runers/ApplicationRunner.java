package ru.otus.hw.runers;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.hw.service.SportCarService;

@Component
@RequiredArgsConstructor
public class ApplicationRunner implements CommandLineRunner {
    private final SportCarService service;

    @Override
    public void run(String... args) {
        service.runProcess();
    }
}
