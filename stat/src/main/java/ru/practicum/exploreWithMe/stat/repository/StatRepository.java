package ru.practicum.exploreWithMe.stat.repository;

import ru.practicum.exploreWithMe.stat.dto.HitDto;
import ru.practicum.exploreWithMe.stat.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository {

    Stat save(Stat stats);

    List<HitDto> getAll(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique, String app);
}