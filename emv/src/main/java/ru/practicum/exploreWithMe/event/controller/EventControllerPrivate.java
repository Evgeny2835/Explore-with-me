package ru.practicum.exploreWithMe.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.event.dto.EventFullDto;
import ru.practicum.exploreWithMe.event.dto.EventNewDto;
import ru.practicum.exploreWithMe.event.dto.EventShortDto;
import ru.practicum.exploreWithMe.event.dto.EventUpdateDto;
import ru.practicum.exploreWithMe.event.service.EventServicePrivate;
import ru.practicum.exploreWithMe.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventControllerPrivate {
    private final EventServicePrivate service;

    @PostMapping("/{userId}/events")
    public EventFullDto createEvent(
            @PathVariable @Positive int userId,
            @RequestBody @Valid EventNewDto eventNewDto) {
        log.info("В контроллер поступил запрос пользователя с id={} на создание события с параметрами: {}",
                userId, eventNewDto);
        return service.createEvent(userId, eventNewDto);
    }

    @PatchMapping("/{userId}/events")
    public EventFullDto updateEvent(
            @PathVariable @Positive int userId,
            @RequestBody @Valid EventUpdateDto eventUpdateDto) {
        return service.updateEvent(userId, eventUpdateDto);
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEvents(
            @PathVariable @Positive int userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        return service.getEvents(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventById(
            @PathVariable @Positive int userId,
            @PathVariable @Positive int eventId) {
        return service.getEventById(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto cancel(
            @PathVariable @Positive int userId,
            @PathVariable @Positive int eventId) {
        return service.cancelEvent(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<RequestDto> getRequests(
            @PathVariable @Positive int userId,
            @PathVariable @Positive int eventId) {
        return service.getRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public RequestDto confirmRequest(
            @PathVariable @Positive int userId,
            @PathVariable @Positive int eventId,
            @PathVariable @Positive int reqId) {
        return service.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public RequestDto rejectRequest(
            @PathVariable @Positive int userId,
            @PathVariable @Positive int eventId,
            @PathVariable @Positive int reqId) {
        return service.rejectRequest(userId, eventId, reqId);
    }
}