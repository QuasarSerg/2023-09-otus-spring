package ru.otus.hw.services;

import ru.otus.hw.models.fusionbrain.TextToImageResponse;

public interface FusionbrainService {
    TextToImageResponse textToImage(String query);
}
