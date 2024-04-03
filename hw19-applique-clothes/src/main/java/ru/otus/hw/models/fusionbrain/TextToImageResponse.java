package ru.otus.hw.models.fusionbrain;

import lombok.Data;

import java.util.List;

@Data
public class TextToImageResponse {
    private String uuid;

    private String status;

    private List<String> images;

    private Boolean censored;
}