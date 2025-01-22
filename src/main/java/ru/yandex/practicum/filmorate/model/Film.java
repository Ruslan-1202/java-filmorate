package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.LinkedHashSet;

/**
 * Film.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Film {
    private Long id;
    @NotBlank
    private String name;
    @Size(max = 200, message = "Слишком длинное описание")
    private String description;
    private Mpa mpa;
    private LinkedHashSet<Genre> genres;
    private LocalDate releaseDate;
    private Integer duration;
}
