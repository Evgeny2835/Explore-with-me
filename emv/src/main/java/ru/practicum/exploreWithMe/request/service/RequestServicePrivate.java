package ru.practicum.exploreWithMe.request.service;

import ru.practicum.exploreWithMe.request.dto.RequestDto;

import java.util.List;

public interface RequestServicePrivate {

    RequestDto create(Integer userId, Integer eventId);

    RequestDto cancel(Integer userId, Integer requestId);

    List<RequestDto> getAllByRequester(Integer userId);
}