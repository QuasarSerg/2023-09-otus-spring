package ru.otus.hw.models.fusionbrain;

import lombok.Data;

@Data
public class RunRequest {
    private String type;

    private String style;

    private Integer numImages;

    private Integer width;

    private Integer height;

    private GenerateParams generateParams;
}