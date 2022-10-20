package ru.practicum.exploreWithMe.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserNewDto {
    @NotNull
    @Email
    private String email;
    @NotBlank
    private String name;
}