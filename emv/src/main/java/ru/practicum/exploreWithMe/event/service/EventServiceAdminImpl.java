package ru.practicum.exploreWithMe.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.category.model.Category;
import ru.practicum.exploreWithMe.category.service.CategoryServiceAdmin;
import ru.practicum.exploreWithMe.event.client.HitDto;
import ru.practicum.exploreWithMe.event.client.StatClient;
import ru.practicum.exploreWithMe.event.dto.EventFullDto;
import ru.practicum.exploreWithMe.event.dto.EventUpdateDtoAdmin;
import ru.practicum.exploreWithMe.event.mapper.EventMapper;
import ru.practicum.exploreWithMe.event.model.Event;
import ru.practicum.exploreWithMe.event.model.EventState;
import ru.practicum.exploreWithMe.event.repository.EventJpaRepository;
import ru.practicum.exploreWithMe.exception.exceptions.ConditionException;
import ru.practicum.exploreWithMe.exception.exceptions.NotFoundException;
import ru.practicum.exploreWithMe.request.model.Request;
import ru.practicum.exploreWithMe.request.model.RequestStatus;
import ru.practicum.exploreWithMe.request.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceAdminImpl implements EventServiceAdmin {
    private final EventJpaRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoryServiceAdmin categoryServiceAdmin;
    private final RequestRepository requestRepository;
    private final StatClient statClient;
    private final EventServicePublic eventServicePublic;

    @Override
    public List<EventFullDto> getAll(
            List<Integer> users,
            List<EventState> states,
            List<Integer> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Integer from,
            Integer size) {

        log.info("Start of processing the request for issuing a list of events with parameters: " +
                        "users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);

        PageRequest pageRequest = PageRequest.of(from, size);

        List<Event> events = eventRepository.findByParameters(
                users,
                states,
                categories,
                rangeStart,
                rangeEnd,
                pageRequest);

        return addHitsAndRequestsAndConvertToEventFullDto(events);
    }

    public EventFullDto update(Integer eventId, EventUpdateDtoAdmin eventUpdateDtoAdmin) {
        log.info("Start processing an event update request with parameters: eventId={}, data={}",
                eventId, eventUpdateDtoAdmin);

        Event event = validateEventByIdAndReturnEvent(eventId);

        if (eventUpdateDtoAdmin.getCategory() != null) {
            Category category = categoryServiceAdmin.validateCategoryByIdAndReturnCategory(
                    eventUpdateDtoAdmin.getCategory());
            event.setCategory(category);
        }

        if (eventUpdateDtoAdmin.getAnnotation() != null) {
            event.setAnnotation(eventUpdateDtoAdmin.getAnnotation());
        }

        if (eventUpdateDtoAdmin.getDescription() != null) {
            event.setDescription(eventUpdateDtoAdmin.getDescription());
        }

        if (eventUpdateDtoAdmin.getEventDate() != null) {
            event.setEventDate(eventUpdateDtoAdmin.getEventDate());
        }

        if (eventUpdateDtoAdmin.getLocation() != null) {
            event.setLatitude(eventUpdateDtoAdmin.getLocation().getLat());
            event.setLongitude(eventUpdateDtoAdmin.getLocation().getLon());
        }

        if (eventUpdateDtoAdmin.getPaid() != null) {
            event.setPaid(eventUpdateDtoAdmin.getPaid());
        }

        if (eventUpdateDtoAdmin.getParticipantLimit() != null) {
            event.setParticipantLimit(eventUpdateDtoAdmin.getParticipantLimit());
        }

        if (eventUpdateDtoAdmin.getRequestModeration() != null) {
            event.setRequestModeration(eventUpdateDtoAdmin.getRequestModeration());
        }

        if (eventUpdateDtoAdmin.getTitle() != null) {
            event.setTitle(eventUpdateDtoAdmin.getTitle());
        }

        eventRepository.save(event);

        return eventServicePublic.addHitsAndRequestsAndConvertToEventFullDto(event);
    }

    public EventFullDto publish(Integer eventId) {
        log.info("Start of processing an event publication request: eventId={}", eventId);

        Event event = validateEventByIdAndReturnEvent(eventId);

        if (event.getState() != EventState.PENDING) {
            throw new ConditionException(String.format("The publication of the event is possible with the %s status",
                    EventState.PENDING));
        }

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ConditionException("Start date of the event must be no earlier" +
                    " than an hour from the date of publication");
        }

        event.setState(EventState.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
        eventRepository.save(event);
        return eventServicePublic.addHitsAndRequestsAndConvertToEventFullDto(event);
    }

    public EventFullDto reject(Integer eventId) {
        log.info("Start of processing the event rejection request: eventId={}", eventId);
        Event event = validateEventByIdAndReturnEvent(eventId);
        if (event.getState() != EventState.PENDING) {
            throw new ConditionException(String.format("Event rejection is possible with the event %s status",
                    EventState.PENDING));
        }
        event.setState(EventState.CANCELED);
        eventRepository.save(event);
        return eventServicePublic.addHitsAndRequestsAndConvertToEventFullDto(event);
    }

    private Event validateEventByIdAndReturnEvent(Integer eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event not found, id=%d", eventId)));
    }


    private List<EventFullDto> addHitsAndRequestsAndConvertToEventFullDto(List<Event> events) {
        Map<String, HitDto> statHitsMap = new HashMap<>();

        List<EventFullDto> result = events.stream()
                .map(s -> {
                    statHitsMap.put("/events/" + s.getId(), null);
                    EventFullDto dto = eventMapper.toEventFullDto(s);
                    dto.setConfirmedRequests(getConfirmedRequestsCountForEvent(s));
                    return dto;
                }).collect(Collectors.toList());

        Event eventWithMinCreatedOn = events
                .stream()
                .min(Comparator.comparing(Event::getCreatedOn))
                .orElseThrow();

        statClient.getStat(
                eventWithMinCreatedOn.getCreatedOn(),
                LocalDateTime.now(),
                statHitsMap.keySet(),
                false
        ).forEach(e -> statHitsMap.put(e.getUri(), e));

        result.forEach(p -> {
            String eventUrl = "/events/" + p.getId();
            HitDto hitDto = statHitsMap.get(eventUrl);

            if (hitDto != null) {
                p.setViews(hitDto.getHits());
            } else {
                p.setViews(0);
            }
        });

        return result;
    }

    private int getConfirmedRequestsCountForEvent(Event event) {
        List<Request> requests = requestRepository.findByEventId(event.getId());
        return (int) requests
                .stream()
                .filter(s -> s.getStatus().equals(RequestStatus.CONFIRMED))
                .count();
    }
}