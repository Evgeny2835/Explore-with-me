package ru.practicum.exploreWithMe.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserShortDto {
    private Integer id;
    private String name;
}