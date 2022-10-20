package ru.practicum.exploreWithMe.compilation.service;

import ru.practicum.exploreWithMe.compilation.dto.CompilationDto;
import ru.practicum.exploreWithMe.compilation.dto.CompilationNewDto;

public interface CompilationServiceAdmin {
    CompilationDto createCompilation(CompilationNewDto compilationNewDto);

    void deleteCompilation(Integer compId);

    void deleteEventFromCompilation(Integer compId, Integer eventId);

    CompilationDto addEventToCompilation(Integer compId, Integer eventId);

    void unpin(Integer compId);

    void pin(Integer compId);
}