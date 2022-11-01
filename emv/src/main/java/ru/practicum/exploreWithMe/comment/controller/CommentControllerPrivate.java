package ru.practicum.exploreWithMe.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.comment.dto.CommentFullDto;
import ru.practicum.exploreWithMe.comment.dto.CommentNewDto;
import ru.practicum.exploreWithMe.comment.dto.CommentUpdateDto;
import ru.practicum.exploreWithMe.comment.service.CommentServicePrivate;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CommentControllerPrivate {
    private final CommentServicePrivate commentServicePrivate;

    @PostMapping
    public CommentFullDto create(
            @PathVariable @Positive int userId,
            @RequestBody @Valid CommentNewDto commentNewDto) {
        log.info("Create comment with parameters: userId={}, data={}",
                userId, commentNewDto);
        return commentServicePrivate.create(userId, commentNewDto);
    }

    @PutMapping
    public CommentFullDto update(
            @PathVariable @Positive int userId,
            @RequestBody @Valid CommentUpdateDto commentUpdateDto) {
        log.info("Update comment with parameters: userId={}, data={}",
                userId, commentUpdateDto);
        return commentServicePrivate.update(userId, commentUpdateDto);
    }

    @DeleteMapping("/{commentId}")
    public void delete(
            @PathVariable @Positive int userId,
            @PathVariable @Positive int commentId) {
        log.info("Delete comment with parameters: userId={}, commentId={}",
                userId, commentId);
        commentServicePrivate.delete(userId, commentId);
    }
}