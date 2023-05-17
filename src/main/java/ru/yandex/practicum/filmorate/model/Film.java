package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Film {
    private final int id;
    private final String title;
    private final String description;
    private final LocalDateTime releaseDate;
    private final int duration;
}
