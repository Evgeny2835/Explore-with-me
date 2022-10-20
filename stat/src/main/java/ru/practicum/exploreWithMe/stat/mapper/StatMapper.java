package ru.practicum.exploreWithMe.stat.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.stat.dto.StatDto;
import ru.practicum.exploreWithMe.stat.model.Stat;

@Component
public class StatMapper {
    public StatDto toStatDto(Stat stat) {
        return StatDto.builder()
                .id(stat.getId())
                .app(stat.getApp())
                .uri(stat.getUri())
                .ip(stat.getIp())
                .timestamp(stat.getCreated())
                .build();
    }

    public Stat toStat(StatDto statDto) {
        return Stat.builder()
                .app(statDto.getApp())
                .uri(statDto.getUri())
                .ip(statDto.getIp())
                .build();
    }
}