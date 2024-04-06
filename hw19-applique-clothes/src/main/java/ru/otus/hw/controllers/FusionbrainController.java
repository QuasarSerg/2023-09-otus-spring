package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.models.fusionbrain.TextToImageResponse;
import ru.otus.hw.services.FusionbrainService;

@RequiredArgsConstructor
@RestController
public class FusionbrainController {
    private final FusionbrainService fusionbrainService;

    @GetMapping("/api/v1/textToImage/{query}")
    public TextToImageResponse textToImage(@PathVariable("query") String query) {
        return fusionbrainService.textToImage(query);
    }
}