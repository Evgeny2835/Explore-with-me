package ru.practicum.exploreWithMe.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.event.dto.EventFullDto;
import ru.practicum.exploreWithMe.event.dto.EventUpdateDtoAdmin;
import ru.practicum.exploreWithMe.event.model.EventState;
import ru.practicum.exploreWithMe.event.service.EventServiceAdmin;
import ru.practicum.exploreWithMe.exception.exceptions.ValidationException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventControllerAdmin {
    private final EventServiceAdmin eventServiceAdmin;

    @GetMapping
    public List<EventFullDto> getAll(
            @RequestParam(required = false) List<Integer> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {

        List<EventState> eventStates;
        if (states != null) {
            eventStates = states.stream()
                    .map(this::validateParamAndReturnEventStates)
                    .collect(Collectors.toList());
        } else {
            eventStates = null;
        }

        log.info("Request to issue a list of events has been submitted for processing, " +
                        "users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);

        return eventServiceAdmin.getAll(users, eventStates, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{eventId}")
    public EventFullDto update(
            @PathVariable @Positive int eventId,
            @RequestBody @Valid EventUpdateDtoAdmin eventUpdateDtoAdmin) {
        log.info("Event update request has been sent for processing, eventId={}, data={}",
                eventId, eventUpdateDtoAdmin);
        return eventServiceAdmin.update(eventId, eventUpdateDtoAdmin);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publish(
            @PathVariable @Positive int eventId) {
        log.info("Request to publish the event has been sent for processing, eventId={}", eventId);
        return eventServiceAdmin.publish(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto reject(
            @PathVariable @Positive int eventId) {
        log.info("Request to reject the event has been sent for processing, eventId={}", eventId);
        return eventServiceAdmin.reject(eventId);
    }

    private EventState validateParamAndReturnEventStates(String state) {
        return EventState.from(state).orElseThrow(() ->
                new ValidationException("Unknown state: " + state));
    }
}