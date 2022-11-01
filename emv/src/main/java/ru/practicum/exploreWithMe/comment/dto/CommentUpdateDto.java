package ru.practicum.exploreWithMe.comment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentUpdateDto {

    @NotNull
    @Positive
    private Integer commentId;

    @NotBlank
    @Size(max = 10000)
    private String text;
}