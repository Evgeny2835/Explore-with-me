package ru.practicum.exploreWithMe.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exploreWithMe.compilation.dto.CompilationDto;
import ru.practicum.exploreWithMe.compilation.dto.CompilationNewDto;
import ru.practicum.exploreWithMe.compilation.mapper.CompilationMapper;
import ru.practicum.exploreWithMe.compilation.model.Compilation;
import ru.practicum.exploreWithMe.compilation.repository.CompilationRepository;
import ru.practicum.exploreWithMe.event.model.Event;
import ru.practicum.exploreWithMe.event.repository.EventJpaRepository;
import ru.practicum.exploreWithMe.exception.exceptions.ConditionException;
import ru.practicum.exploreWithMe.exception.exceptions.ConflictException;
import ru.practicum.exploreWithMe.exception.exceptions.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CompilationServiceAdminImpl implements CompilationServiceAdmin {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;

    private final EventJpaRepository eventJpaRepository;

    @Override
    public CompilationDto createCompilation(CompilationNewDto compilationNewDto) {
        log.info("Request to create a compilation of events, body={}", compilationNewDto);
        Compilation compilation = compilationMapper.toCompilation(compilationNewDto);
        List<Event> events = eventJpaRepository.findAllByIdIn(compilationNewDto.getEvents(), null);
        compilation.setEvents(events);
        return compilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(Integer compId) {
        log.info("Request to delete a compilation of events, compId={}", compId);
        validateAndReturnCompilation(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public void deleteEventFromCompilation(Integer compId, Integer eventId) {
        log.info("Delete an event from a compilation, compId={}, eventId={}", compId, eventId);
        Compilation compilation = validateAndReturnCompilation(compId);
        Event event = validateAndReturnEvent(eventId);
        if (!compilation.getEvents().contains(event)) {
            throw new ConditionException(String.format("Event not found in compilation, compId=%d, eventId=%d",
                    compId, eventId));
        }
        compilation.getEvents().remove(event);
        compilationRepository.save(compilation);
    }

    @Override
    public CompilationDto addEventToCompilation(Integer compId, Integer eventId) {
        log.info("Add event to compilation, compId={}, eventId={}", compId, eventId);
        Compilation compilation = validateAndReturnCompilation(compId);
        Event event = validateAndReturnEvent(eventId);
        if (compilation.getEvents().contains(event)) {
            throw new ConflictException(String.format("Event is already in the compilation, eventId=%d",
                    eventId));
        }
        compilation.getEvents().add(event);
        return compilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void unpin(Integer compId) {
        log.info("Unpin the compilation, compId={}", compId);
        Compilation compilation = validateAndReturnCompilation(compId);
        if (!compilation.getPinned()) {
            throw new ConflictException(String.format("Pinned is already FALSE, compId=%d", compId));
        }
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    public void pin(Integer compId) {
        log.info("Pin the compilation, compId={}", compId);
        Compilation compilation = validateAndReturnCompilation(compId);
        if (compilation.getPinned()) {
            throw new ConflictException(String.format("Pinned is already TRUE, compId=%d", compId));
        }
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    private Compilation validateAndReturnCompilation(Integer compId) {
        return compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException(String.format("Compilation not found, id=%d", compId)));
    }

    private Event validateAndReturnEvent(Integer eventId) {
        return eventJpaRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event not found, id=%d", eventId)));
    }
}