package ru.practicum.exploreWithMe.stat.service;

import ru.practicum.exploreWithMe.stat.dto.HitDto;
import ru.practicum.exploreWithMe.stat.dto.StatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {

    StatDto save(StatDto statDto);

    List<HitDto> getAll(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique, String app);
}