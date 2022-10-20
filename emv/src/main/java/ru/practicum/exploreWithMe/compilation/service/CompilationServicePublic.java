package ru.practicum.exploreWithMe.compilation.service;

import ru.practicum.exploreWithMe.compilation.dto.CompilationDto;

import java.util.List;

public interface CompilationServicePublic {

    List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size);

    CompilationDto getById(Integer compId);
}