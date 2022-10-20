package ru.practicum.exploreWithMe.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.compilation.dto.CompilationDto;
import ru.practicum.exploreWithMe.compilation.service.CompilationServicePublic;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompilationControllerPublic {
    private final CompilationServicePublic compilationServicePublic;

    @GetMapping
    public List<CompilationDto> getAll(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.info("Get compilations of events, pinned={}, from={}, size={}", pinned, from, size);
        return compilationServicePublic.getAll(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getById(
            @PathVariable @Positive int compId) {
        log.info("Get compilation of events, compId={}", compId);
        return compilationServicePublic.getById(compId);
    }
}