package ru.practicum.exploreWithMe.event.model;

import java.util.Optional;

public enum EventSort {
    EVENT_DATE,
    VIEWS;

    public static Optional<EventSort> from(String typeSort) {
        for (EventSort sort : values()) {
            if (sort.name().equalsIgnoreCase(typeSort)) {
                return Optional.of(sort);
            }
        }
        return Optional.empty();
    }
}