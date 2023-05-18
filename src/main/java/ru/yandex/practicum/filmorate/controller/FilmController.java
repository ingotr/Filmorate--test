package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private int idCounter = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Map<Integer, Film> getAllFilms() {
        log.debug("Получен запрос GET /films.");
        log.debug("Текущее количество фильмов: {}", films.size());
        return films;
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        log.debug("Получен запрос POST /films.");
        if (film != null) {
            isFilmTitleValid(film);
        } else {
            log.warn("Отправлен пустой запрос");
            throw new ValidationException("Отправлен пустой запрос");
        }
        log.debug("Добавлен новый фильм с Id: " + film.getId());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.debug("Получен запрос PUT /films.");
        if (film != null) {
            isUpdatedFilmHasId(film);
        } else {
            log.warn("Отправлен пустой запрос");
            throw new ValidationException("Отправлен пустой запрос");
        }

        return film;
    }

    private void isUpdatedFilmHasId(Film film) {
        if (film.getId() != null) {
            films.replace(film.getId(), film);
        } else {
            log.warn("Не указан Id фильма");
            throw new ValidationException("Не указан Id фильма. Укажите и попробуйте снова");
        }
        log.debug("Обновлен фильм с Id: " + film.getId());
    }

    private void isFilmTitleValid(Film film) {
        if (!film.getTitle().isBlank()) {
            isFilmDescriptionLengthValid(film);
        } else {
            log.warn("Не указано название фильма");
            throw new ValidationException("Не указано название фильма. Укажите и попробуйте снова");
        }
    }

    private void isFilmDescriptionLengthValid(Film film) {
        if (film.getDescription().length() < 200 && !film.getDescription().isBlank()) {
            isFilmReleaseDateNotEmpty(film);
        } else {
            log.warn("Длина описания больше 200 символов или пустое");
            throw new ValidationException("Максимальная длина описания - 200 символов. Или пустая");
        }
    }

    private void isFilmReleaseDateNotEmpty(Film film) {
        if (film.getReleaseDate().isPresent()) {
            isFilmReleaseDateValid(film);
        }
        else {
            log.warn("Дата релиза не указана");
        }
    }

    private void isFilmReleaseDateValid(Film film) {
        if (!film.getReleaseDate().get().isBefore(LocalDateTime.of(1895, 12, 28, 1, 1))) {
            isFilmDurationPositive(film);
        } else {
            log.warn("Дата релиза раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — не может быть раньше 28 декабря 1895 года.");
        }
    }

    private void isFilmDurationPositive(Film film) {
        if (film.getDuration() > 0) {
            films.put(getIdCounter(), film);
        } else {
            log.warn("Продолжительность фильма отрицательная или равна 0");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }

    public int getIdCounter() {
        return idCounter++;
    }
}
