package ru.practicum.exploreWithMe.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.category.model.Category;
import ru.practicum.exploreWithMe.category.service.CategoryServiceAdmin;
import ru.practicum.exploreWithMe.event.dto.EventFullDto;
import ru.practicum.exploreWithMe.event.dto.EventNewDto;
import ru.practicum.exploreWithMe.event.dto.EventShortDto;
import ru.practicum.exploreWithMe.event.dto.EventUpdateDto;
import ru.practicum.exploreWithMe.event.mapper.EventMapper;
import ru.practicum.exploreWithMe.event.model.Event;
import ru.practicum.exploreWithMe.event.model.EventState;
import ru.practicum.exploreWithMe.event.repository.EventJpaRepository;
import ru.practicum.exploreWithMe.exception.exceptions.ConditionException;
import ru.practicum.exploreWithMe.exception.exceptions.ConflictException;
import ru.practicum.exploreWithMe.exception.exceptions.NotFoundException;
import ru.practicum.exploreWithMe.request.dto.RequestDto;
import ru.practicum.exploreWithMe.request.mapper.RequestMapper;
import ru.practicum.exploreWithMe.request.model.Request;
import ru.practicum.exploreWithMe.request.model.RequestStatus;
import ru.practicum.exploreWithMe.request.repository.RequestRepository;
import ru.practicum.exploreWithMe.user.model.User;
import ru.practicum.exploreWithMe.user.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServicePrivateImpl implements EventServicePrivate {
    private final EventJpaRepository eventJpaRepository;
    private final EventMapper eventMapper;
    private final UserService userService;
    private final EventServicePublic eventServicePublic;
    private final CategoryServiceAdmin categoryServiceAdmin;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private static final int MINIMUM_HOURS_TO_EVENT = 2;

    @Override
    public EventFullDto createEvent(Integer userId, EventNewDto eventNewDto) {
        log.info("A request was received to create an event with parameters: userId={}, data={}",
                userId, eventNewDto);

        LocalDateTime currentDate = LocalDateTime.now();

        validateEventDate(eventNewDto.getEventDate(), currentDate);

        Event event = eventMapper.toEvent(eventNewDto);

        User user = userService.get(userId);

        event.setInitiator(user);

        event.setCreatedOn(currentDate);

        event.setState(EventState.PENDING);

        event = eventJpaRepository.save(event);

        return eventServicePublic.addHitsAndRequestsAndConvertToEventFullDto(event);
    }

    @Override
    public EventFullDto updateEvent(Integer userId, EventUpdateDto eventUpdateDto) {
        log.info("A request was received to update the event with the parameters: userId={}, data={}",
                userId, eventUpdateDto);

        User user = userService.get(userId);

        Event event = getById(eventUpdateDto.getEventId());

        LocalDateTime currentDate = LocalDateTime.now();

        validateUserIsEventInitiator(user, event);

        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException(String.format("Published event can not be changed, eventStatus=%s",
                    EventState.PUBLISHED));
        }

        if (event.getState() == EventState.CANCELED) {
            event.setState(EventState.PENDING);
        }

        if (eventUpdateDto.getAnnotation() != null) {
            event.setAnnotation(eventUpdateDto.getAnnotation());
        }

        if (eventUpdateDto.getCategory() != null) {
            Category category = categoryServiceAdmin.validateCategoryByIdAndReturnCategory(
                    eventUpdateDto.getCategory());
            event.setCategory(category);
        }

        if (eventUpdateDto.getDescription() != null) {
            event.setDescription(eventUpdateDto.getDescription());
        }

        if (eventUpdateDto.getEventDate() != null) {
            validateEventDate(eventUpdateDto.getEventDate(), currentDate);
            event.setEventDate(eventUpdateDto.getEventDate());
        }

        if (eventUpdateDto.getPaid() != null) {
            event.setPaid(eventUpdateDto.getPaid());
        }

        if (eventUpdateDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventUpdateDto.getParticipantLimit());
        }

        if (eventUpdateDto.getTitle() != null) {
            event.setTitle(eventUpdateDto.getTitle());
        }

        event = eventJpaRepository.save(event);

        return eventServicePublic.addHitsAndRequestsAndConvertToEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getEvents(Integer userId, Integer from, Integer size) {
        log.info("A request has been received to receive events initiated by the current user " +
                "with the parameters: userId={}, from={}, size={}", userId, from, size);

        User user = userService.get(userId);

        PageRequest pageRequest = PageRequest.of(from, size);

        List<Event> events = eventJpaRepository.findAllByInitiatorId(user.getId(), pageRequest);

        return eventServicePublic.addHitsAndRequestsAndConvertToEventShortDto(events);
    }

    @Override
    public EventFullDto getEventById(Integer userId, Integer eventId) {
        log.info("Request to receive an event with parameters: userId={}, eventId={}",
                userId, eventId);

        User user = userService.get(userId);

        Event event = getById(eventId);

        validateUserIsEventInitiator(user, event);

        return eventServicePublic.addHitsAndRequestsAndConvertToEventFullDto(event);
    }

    @Override
    public EventFullDto cancelEvent(Integer userId, Integer eventId) {
        log.info("Request to cancel an event with parameters: userId={}, eventId={}",
                userId, eventId);

        User user = userService.get(userId);

        Event event = getById(eventId);

        validateUserIsEventInitiator(user, event);

        if (event.getState() != EventState.PENDING) {
            throw new ConditionException(String.format("Cancellation of the event is possible with the %s status",
                    EventState.PENDING));
        }

        event.setState(EventState.CANCELED);

        event = eventJpaRepository.save(event);

        return eventServicePublic.addHitsAndRequestsAndConvertToEventFullDto(event);
    }

    @Override
    public List<RequestDto> getRequests(Integer userId, Integer eventId) {
        log.info("Request for information about requests to participate in the event of the current user, " +
                "userId={}, eventId={}", userId, eventId);

        User user = userService.get(userId);

        Event event = getById(eventId);

        validateUserIsEventInitiator(user, event);

        return requestRepository.findByEventId(eventId)
                .stream()
                .filter(p -> !p.getRequester().getId().equals(userId))
                .map(requestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto confirmRequest(Integer userId, Integer eventId, Integer reqId) {
        log.info("Request for confirmation of the request, userId={}, eventId={}, reqId={}",
                userId, eventId, reqId);

        User user = userService.get(userId);

        Event event = getById(eventId);

        validateUserIsEventInitiator(user, event);

        Request request = requestRepository.findById(reqId).orElseThrow(() ->
                new NotFoundException(String.format("Request not found, id=%d", reqId)));

        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            throw new ConditionException(String.format("Application confirmation is not required if the " +
                    "participant limit is 0 or pre-moderation is disabled, participantLimit=%s, " +
                    "requestModeration=%s", event.getParticipantLimit(), event.getRequestModeration()));
        }

        if (eventServicePublic.isRequestLimit(event)) {
            throw new ConflictException(String.format(
                    "Request limit has been exhausted, participantLimit=%d", event.getParticipantLimit()));
        }

        request.setStatus(RequestStatus.CONFIRMED);

        requestRepository.save(request);

        if (eventServicePublic.isRequestLimit(event)) {
            requestRepository.findAllByEventId(event.getId())
                    .forEach((k) -> {
                        if (k.getStatus().equals(RequestStatus.PENDING)) {
                            k.setStatus(RequestStatus.REJECTED);
                        }
                    });
        }

        return requestMapper.toRequestDto(request);
    }

    @Override
    public RequestDto rejectRequest(Integer userId, Integer eventId, Integer reqId) {
        log.info("Request to reject a request to participate in an event, userId={}, eventId={}, reqId={}",
                userId, eventId, reqId);

        User user = userService.get(userId);

        Event event = getById(eventId);

        validateUserIsEventInitiator(user, event);

        Request request = requestRepository.findById(reqId).orElseThrow(() ->
                new NotFoundException(String.format("Request not found, id=%d", reqId)));

        request.setStatus(RequestStatus.REJECTED);

        requestRepository.save(request);

        return requestMapper.toRequestDto(request);
    }

    private void validateEventDate(LocalDateTime eventDate, LocalDateTime currentDate) {
        LocalDateTime minEventDate = currentDate.plusHours(MINIMUM_HOURS_TO_EVENT);
        if (minEventDate.isAfter(eventDate)) {
            throw new ConditionException(String.format("Дата и время, на которые намечено событие, не могут быть " +
                            "раньше, чем через два часа от текущего момента, текущая дата %s, дата события %s",
                    currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    eventDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        }
    }

    private Event getById(Integer eventId) {
        return eventJpaRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event not found, id=%d", eventId)));
    }

    private void validateUserIsEventInitiator(User user, Event event) {
        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new ConditionException(String.format("User is not the initiator of the event, " +
                    "userId=%d, eventInitiatorId=%d", user.getId(), event.getInitiator().getId()));
        }
    }
}