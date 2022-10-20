package ru.practicum.exploreWithMe.event.model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class Location {
    Double lat;
    Double lon;
}