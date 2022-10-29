package ru.practicum.exploreWithMe.event.service;

import ru.practicum.exploreWithMe.event.dto.EventFullDto;
import ru.practicum.exploreWithMe.event.dto.EventShortDto;
import ru.practicum.exploreWithMe.event.model.Event;
import ru.practicum.exploreWithMe.event.model.EventSort;

import java.time.LocalDateTime;
import java.util.List;

public interface EventServicePublic {

    List<EventShortDto> getAll(
            String text,
            List<Integer> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            EventSort eventSort,
            Integer from,
            Integer size);

    EventFullDto getById(Integer eventId);

    EventFullDto addHitsAndRequestsAndConvertToEventFullDto(Event event);

    List<EventShortDto> addHitsAndRequestsAndConvertToEventShortDto(List<Event> events);

    boolean isRequestLimit(Event event);
}