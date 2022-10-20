package ru.practicum.exploreWithMe.compilation.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.compilation.dto.CompilationDto;
import ru.practicum.exploreWithMe.compilation.dto.CompilationNewDto;
import ru.practicum.exploreWithMe.compilation.model.Compilation;
import ru.practicum.exploreWithMe.event.dto.EventShortDto;
import ru.practicum.exploreWithMe.event.mapper.EventMapper;
import ru.practicum.exploreWithMe.event.model.Event;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final EventMapper eventMapper;

    public Compilation toCompilation(CompilationNewDto newDto) {
        return Compilation.builder()
                .pinned(newDto.getPinned())
                .title(newDto.getTitle())
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation) {

        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(convertToEventsShortDto(compilation.getEvents()))
                .build();
    }

    private List<EventShortDto> convertToEventsShortDto(List<Event> events) {
        return events.stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }
}