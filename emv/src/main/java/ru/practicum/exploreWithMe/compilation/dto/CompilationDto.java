package ru.practicum.exploreWithMe.compilation.dto;

import lombok.*;
import ru.practicum.exploreWithMe.event.dto.EventShortDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompilationDto {
    private Integer id;
    private Boolean pinned;
    private String title;
    List<EventShortDto> events;
}