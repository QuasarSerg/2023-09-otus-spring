package ru.otus.hw.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.hw.models.SportCar;
import ru.otus.hw.service.SportCarService;


@Configuration
public class IntegrationConfig {

    @Bean
    public MessageChannelSpec<?, ?> carChannel() {
        return MessageChannels.queue(5);
    }

    @Bean
    public MessageChannelSpec<?, ?> sportCarChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(1000).maxMessagesPerPoll(2);
    }

    @Bean
    public IntegrationFlow sportCarFlow(SportCarService sportCarService) {
        return IntegrationFlow.from(carChannel())
                .split()
                .handle(sportCarService, "transform")
                .<SportCar, SportCar>transform(car -> new SportCar(car.getName(), car.getModifications()))
                .aggregate()
                .channel(sportCarChannel())
                .get();
    }
}