package ru.practicum.exploreWithMe.event.service;

import ru.practicum.exploreWithMe.event.dto.EventFullDto;
import ru.practicum.exploreWithMe.event.dto.EventUpdateDtoAdmin;
import ru.practicum.exploreWithMe.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventServiceAdmin {

    List<EventFullDto> getAll(
            List<Integer> users,
            List<EventState> states,
            List<Integer> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Integer from,
            Integer size
    );

    EventFullDto update(Integer eventId, EventUpdateDtoAdmin eventUpdateDtoAdmin);

    EventFullDto publish(Integer eventId);

    EventFullDto reject(Integer eventId);
}