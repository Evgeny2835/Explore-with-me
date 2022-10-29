package ru.practicum.exploreWithMe.event.service;

import ru.practicum.exploreWithMe.event.dto.EventFullDto;
import ru.practicum.exploreWithMe.event.dto.EventNewDto;
import ru.practicum.exploreWithMe.event.dto.EventShortDto;
import ru.practicum.exploreWithMe.event.dto.EventUpdateDto;
import ru.practicum.exploreWithMe.request.dto.RequestDto;

import java.util.List;

public interface EventServicePrivate {

    EventFullDto createEvent(Integer userId, EventNewDto eventNewDto);

    EventFullDto updateEvent(Integer userId, EventUpdateDto eventUpdateDto);

    List<EventShortDto> getEvents(Integer userId, Integer from, Integer size);

    EventFullDto getEventById(Integer userId, Integer eventId);

    EventFullDto cancelEvent(Integer userId, Integer eventId);

    List<RequestDto> getRequests(Integer userId, Integer eventId);

    RequestDto confirmRequest(Integer userId, Integer eventId, Integer reqId);

    RequestDto rejectRequest(Integer userId, Integer eventId, Integer reqId);
}