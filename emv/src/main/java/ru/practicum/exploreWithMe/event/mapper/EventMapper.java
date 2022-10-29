package ru.practicum.exploreWithMe.event.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.category.dto.CategoryDto;
import ru.practicum.exploreWithMe.category.model.Category;
import ru.practicum.exploreWithMe.category.service.CategoryServicePublic;
import ru.practicum.exploreWithMe.event.dto.EventFullDto;
import ru.practicum.exploreWithMe.event.dto.EventNewDto;
import ru.practicum.exploreWithMe.event.dto.EventShortDto;
import ru.practicum.exploreWithMe.event.model.Event;
import ru.practicum.exploreWithMe.event.model.Location;
import ru.practicum.exploreWithMe.user.dto.UserShortDto;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final CategoryServicePublic publicCategoryService;

    public Event toEvent(EventNewDto eventNewDto) {
        CategoryDto categoryDto = publicCategoryService.getById(eventNewDto.getCategory());

        Category category = new Category(
                categoryDto.getId(),
                categoryDto.getName());

        return Event.builder()
                .annotation(eventNewDto.getAnnotation())
                .category(category)
                .description(eventNewDto.getDescription())
                .eventDate(eventNewDto.getEventDate())
                .latitude(eventNewDto.getLocation().getLat())
                .longitude(eventNewDto.getLocation().getLon())
                .paid(eventNewDto.getPaid())
                .participantLimit(eventNewDto.getParticipantLimit() != null ? eventNewDto.getParticipantLimit() : null)
                .requestModeration(eventNewDto.getRequestModeration())
                .title(eventNewDto.getTitle())
                .build();
    }

    public EventFullDto toEventFullDto(Event event) {
        CategoryDto category = CategoryDto.builder()
                .id(event.getCategory().getId())
                .name(event.getCategory().getName())
                .build();

        UserShortDto initiator = new UserShortDto(
                event.getInitiator().getId(),
                event.getInitiator().getName());

        Location location = Location.builder()
                .lat(event.getLatitude())
                .lon(event.getLongitude())
                .build();

        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(category)
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(initiator)
                .location(location)
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .build();
    }

    public EventShortDto toEventShortDto(Event event) {
        CategoryDto category = CategoryDto.builder()
                .id(event.getCategory().getId())
                .name(event.getCategory().getName())
                .build();

        UserShortDto initiator = new UserShortDto(
                event.getInitiator().getId(),
                event.getInitiator().getName());

        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(category)
                .eventDate(event.getEventDate())
                .initiator(initiator)
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }
}