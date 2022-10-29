package ru.practicum.exploreWithMe.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserFullDto {
    private Integer id;
    private String email;
    private String name;
}