package ru.practicum.exploreWithMe.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServicePrivateImpl implements RequestServicePrivate {
    private final RequestRepository requestRepository;
    private final EventJpaRepository eventRepository;
    private final UserService userService;
    private final RequestMapper requestMapper;

    @Override
    public RequestDto create(Integer userId, Integer eventId) {
        log.info("Received a request to add a request to participate in the event, " +
                "userId={}, eventId={}", userId, eventId);

        User user = userService.get(userId);
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event not found, id=%d", eventId)));

        checkUniquenessRequest(userId, eventId);
        checkUserIsNotEventInitiator(user, event);
        checkEventStateIsPublished(event);
        checkRequestLimit(event);

        Request newRequest = new Request();
        newRequest.setEvent(event);
        newRequest.setRequester(user);
        newRequest.setCreated(LocalDateTime.now());

        if (!event.getRequestModeration()) {
            newRequest.setStatus(RequestStatus.CONFIRMED);
        } else {
            newRequest.setStatus(RequestStatus.PENDING);
        }
        return requestMapper.toRequestDto(requestRepository.save(newRequest));
    }

    @Override
    public RequestDto cancel(Integer userId, Integer requestId) {
        log.info("Received a request to cancel your request to participate in the event, " +
                "userId={}, requestId={}", userId, requestId);
        User user = userService.get(userId);
        Request request = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException(String.format("Request not found, id=%d", requestId)));

        if (!user.getId().equals(request.getRequester().getId())) {
            throw new ConditionException(String.format(
                    "It is possible to cancel only your requests, " +
                            "userId=%d, requesterId=%d", user.getId(), request.getRequester().getId()));
        }
        request.setStatus(RequestStatus.CANCELED);
        return requestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public List<RequestDto> getAllByRequester(Integer userId) {
        log.info("A request was received to issue requests from the current user to participate" +
                " in other people's events, userId={}", userId);
        User user = userService.get(userId);
        return requestRepository.findAllByRequesterId(user.getId())
                .stream()
                .filter(s -> !s.getEvent().getInitiator().getId().equals(user.getId()))
                .map(requestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    private void checkUniquenessRequest(Integer userId, Integer eventId) {
        requestRepository.findByRequesterIdAndEventId(userId, eventId)
                .ifPresent(s -> {
                    throw new ConflictException(String.format("Request already exist, userId=%d, eventId=%d",
                            userId, eventId));
                });
    }

    private void checkUserIsNotEventInitiator(User user, Event event) {
        if (user.getId().equals(event.getInitiator().getId())) {
            throw new ConditionException(String.format(
                    "The initiator of the event cannot add a request to participate in his event, " +
                            "userId=%d, eventId=%d", user.getId(), event.getInitiator().getId()));
        }
    }

    private void checkEventStateIsPublished(Event event) {
        if (event.getState() != EventState.PUBLISHED) {
            throw new ConditionException(String.format("It is possible to take part only in published events, " +
                    "eventState=%s", event.getState().toString()));
        }
    }

    private void checkRequestLimit(Event event) {
        int requestCount = requestRepository.findByEventId(event.getId()).size();
        if (event.getParticipantLimit() > 0 && requestCount >= event.getParticipantLimit()) {
            throw new ConditionException(String.format("Request limit exceeded, numberOfRequests=%d, requestLimit=%d",
                    requestCount, event.getParticipantLimit()));
        }
    }
}