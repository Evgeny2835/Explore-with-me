package ru.practicum.exploreWithMe.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.event.client.HitDto;
import ru.practicum.exploreWithMe.event.client.StatClient;
import ru.practicum.exploreWithMe.event.dto.EventFullDto;
import ru.practicum.exploreWithMe.event.dto.EventShortDto;
import ru.practicum.exploreWithMe.event.mapper.EventMapper;
import ru.practicum.exploreWithMe.event.model.Event;
import ru.practicum.exploreWithMe.event.model.EventSort;
import ru.practicum.exploreWithMe.event.model.EventState;
import ru.practicum.exploreWithMe.event.repository.EventCriteriaRepository;
import ru.practicum.exploreWithMe.event.repository.EventJpaRepository;
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
public class EventServicePublicImpl implements EventServicePublic {
    private final EventMapper eventMapper;
    private final EventCriteriaRepository eventCriteriaRepository;
    private final RequestRepository requestRepository;
    private final StatClient statClient;
    private final EventJpaRepository eventJpaRepository;

    @Override
    public List<EventShortDto> getAll(
            String text,
            List<Integer> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            EventSort sort,
            Integer from,
            Integer size) {

        List<Event> events = eventCriteriaRepository.findAllByParams(
                text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                EventState.PUBLISHED,
                from,
                size);

        if (onlyAvailable != null && onlyAvailable) {
            events = events
                    .stream()
                    .filter(s -> !isRequestLimit(s))
                    .collect(Collectors.toList());
        }

        List<EventShortDto> result = addHitsAndRequestsAndConvertToEventShortDto(events);

        return sort(result, sort);
    }

    @Override
    public EventFullDto getById(Integer eventId) {
        Event event = eventJpaRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event not found, id=%d", eventId)));

        return addHitsAndRequestsAndConvertToEventFullDto(event);
    }

    @Override
    public EventFullDto addHitsAndRequestsAndConvertToEventFullDto(Event event) {
        Set<String> uri = Set.of("/events/" + event.getId());

        LocalDateTime rangeStart = event.getCreatedOn();

        LocalDateTime rangeEnd = LocalDateTime.now();

        Optional<HitDto> hitDto = statClient.getStat(rangeStart, rangeEnd, uri, false)
                .stream().findFirst();

        EventFullDto result = eventMapper.toEventFullDto(event);

        hitDto.ifPresent(p -> result.setViews(p.getHits()));

        result.setConfirmedRequests(getConfirmedRequestsCountForEvent(event));

        return result;
    }

    @Override
    public List<EventShortDto> addHitsAndRequestsAndConvertToEventShortDto(List<Event> events) {
        Map<String, HitDto> statHitsMap = new HashMap<>();

        List<EventShortDto> result = events.stream()
                .map(s -> {
                    statHitsMap.put("/events/" + s.getId(), null);
                    EventShortDto dto = eventMapper.toEventShortDto(s);
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

    @Override
    public boolean isRequestLimit(Event event) {
        if (event.getParticipantLimit() == 0 || event.getParticipantLimit() == null) return false;

        long confirmedRequestsCount = requestRepository.findByEventId(event.getId())
                .stream()
                .filter(x -> x.getStatus() == RequestStatus.CONFIRMED)
                .count();

        return confirmedRequestsCount >= event.getParticipantLimit();
    }

    private int getConfirmedRequestsCountForEvent(Event event) {
        List<Request> requests = requestRepository.findByEventId(event.getId());
        return (int) requests
                .stream()
                .filter(s -> s.getStatus().equals(RequestStatus.CONFIRMED))
                .count();
    }

    private List<EventShortDto> sort(List<EventShortDto> result, EventSort sort) {
        if (Objects.isNull(sort)) return result;
        Comparator<EventShortDto> comparator;
        if (sort == EventSort.VIEWS) {
            comparator = Comparator.comparing(EventShortDto::getViews);
        } else {
            comparator = Comparator.comparing(EventShortDto::getEventDate);
        }
        result.sort(comparator);
        return result;
    }
}