package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ru.otus.hw.models.fusionbrain.GenerateParams;
import ru.otus.hw.models.fusionbrain.RunRequest;
import ru.otus.hw.models.fusionbrain.RunResponse;
import ru.otus.hw.models.fusionbrain.TextToImageResponse;

import java.util.concurrent.TimeUnit;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@Service
public class FusionbrainServiceImpl implements FusionbrainService {

    private final WebClient webClient;

    @Override
    public TextToImageResponse textToImage(String query) {
        RunResponse runResponse = runGeneration(query);
        return getTextToImageResponse(runResponse);
    }

    private RunResponse runGeneration(String query) {
        GenerateParams generateParams = new GenerateParams();
        generateParams.setQuery(query);

        RunRequest runRequest = new RunRequest();
        runRequest.setType("GENERATE");
        runRequest.setNumImages(1);
        runRequest.setWidth(512);
        runRequest.setHeight(512);
        runRequest.setGenerateParams(generateParams);

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("model_id", 4);
        builder.part("params", runRequest, MediaType.APPLICATION_JSON);

        return webClient.post()
                .uri("/key/api/v1/text2image/run")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(RunResponse.class)
                .block();
    }

    private TextToImageResponse getTextToImageResponse(RunResponse runResponse) {
        if (nonNull(runResponse)) {
            for (int i = 0; i < 10; i++) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                TextToImageResponse textToImageResponse = webClient.get()
                        .uri("/key/api/v1/text2image/status/{uuid}", runResponse.getUuid())
                        .retrieve()
                        .bodyToMono(TextToImageResponse.class)
                        .block();
                if (nonNull(textToImageResponse) && textToImageResponse.getStatus().equals("DONE")) {
                    return textToImageResponse;
                }
            }
        }
        return null;
    }
}
