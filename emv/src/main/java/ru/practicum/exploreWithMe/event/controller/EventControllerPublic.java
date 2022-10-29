package ru.practicum.exploreWithMe.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.event.client.StatClient;
import ru.practicum.exploreWithMe.event.dto.EventFullDto;
import ru.practicum.exploreWithMe.event.dto.EventShortDto;
import ru.practicum.exploreWithMe.event.model.EventSort;
import ru.practicum.exploreWithMe.event.service.EventServicePublic;
import ru.practicum.exploreWithMe.exception.exceptions.ValidationException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventControllerPublic {
    private final EventServicePublic eventServicePublic;
    private final StatClient statClient;

    @GetMapping
    public List<EventShortDto> getAll(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size,
            HttpServletRequest request
    ) {

        log.info("Request to issue a list of events has been submitted for processing \n" +
                        "text={},\n" +
                        "categories={},\n" +
                        "paid={},\n" +
                        "rangeStart={},\n" +
                        "rangeEnd={},\n" +
                        "onlyAvailable={},\n" +
                        "sort={},\n" +
                        "from={},\n" +
                        "size={},\n",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        EventSort eventSort = EventSort.from(sort).orElseThrow(() ->
                new ValidationException("Unknown typeSort: " + sort));

        statClient.saveStat("ExploreWithMe", request.getRequestURI(), request.getRemoteAddr());

        return eventServicePublic.getAll(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, eventSort, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(
            @PathVariable @Positive int eventId,
            HttpServletRequest request) {

        log.info("Received a request to get event with id={}", eventId);

        statClient.saveStat("ExploreWithMe", request.getRequestURI(), request.getRemoteAddr());

        return eventServicePublic.getById(eventId);
    }
}