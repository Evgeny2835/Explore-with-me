package ru.practicum.exploreWithMe.category.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryNewDto {
    @NotBlank
    @Size(max = 50)
    private String name;
}