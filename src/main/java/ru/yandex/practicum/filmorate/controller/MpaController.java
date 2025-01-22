package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController()
@RequestMapping("/mpa")
@Slf4j
@RequiredArgsConstructor
public class MpaController {

    private final MpaService mpaServicepaService;

    @GetMapping
    public Collection<Mpa> getAll() {
        log.debug("Получение всех записей Mpa");
        return mpaServicepaService.getAll();
    }

    @GetMapping("{id}")
    public Mpa getById(@PathVariable("id") Long id) {
        log.debug("Получение одного Mpa");
        return mpaServicepaService.getById(id);
    }
}
