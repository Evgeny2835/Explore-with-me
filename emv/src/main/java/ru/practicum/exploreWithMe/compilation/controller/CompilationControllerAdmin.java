package ru.practicum.exploreWithMe.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.compilation.dto.CompilationDto;
import ru.practicum.exploreWithMe.compilation.dto.CompilationNewDto;
import ru.practicum.exploreWithMe.compilation.service.CompilationServiceAdmin;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompilationControllerAdmin {
    private final CompilationServiceAdmin compilationServiceAdmin;

    @PostMapping
    public CompilationDto createCompilation(
            @RequestBody @Valid CompilationNewDto compilationNewDto) {
        log.info("Create compilation of events, body={}", compilationNewDto);
        return compilationServiceAdmin.createCompilation(compilationNewDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(
            @PathVariable @Positive int compId) {
        log.info("Delete a compilation of events, compId={}", compId);
        compilationServiceAdmin.deleteCompilation(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(
            @PathVariable @Positive int compId,
            @PathVariable @Positive int eventId) {
        log.info("Delete an event from a compilation, compId={}, eventId={}", compId, eventId);
        compilationServiceAdmin.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public CompilationDto addEventToCompilation(
            @PathVariable @Positive int compId,
            @PathVariable @Positive int eventId) {
        log.info("Add event to compilation, compId={}, eventId={}", compId, eventId);
        return compilationServiceAdmin.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpin(
            @PathVariable @Positive int compId) {
        log.info("Unpin the compilation, compId={}", compId);
        compilationServiceAdmin.unpin(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void pin(
            @PathVariable @Positive int compId) {
        log.info("Pin the compilation, compId={}", compId);
        compilationServiceAdmin.pin(compId);
    }
}