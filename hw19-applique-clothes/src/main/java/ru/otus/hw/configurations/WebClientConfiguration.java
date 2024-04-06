package ru.otus.hw.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {
    private static final String X_KEY_NAME = "X-Key";

    private static final String X_SECRET_NAME = "X-Secret";

    @Value("${web-client.base-url}")
    private String baseUrl;

    @Value("${web-client.x-key}")
    private String xKey;

    @Value("${web-client.x-secret}")
    private String xSecret;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
//                .clientConnector(new ReactorClientHttpConnector(HttpClient.create(ConnectionProvider.newConnection())))
                .baseUrl(baseUrl)
                .defaultHeader(X_KEY_NAME, xKey)
                .defaultHeader(X_SECRET_NAME, xSecret)
                .build();
    }
}
