package ru.practicum.exploreWithMe.compilation.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompilationNewDto {
    private List<Integer> events;
    private Boolean pinned;
    @NotBlank
    @Size(max = 255)
    private String title;
}