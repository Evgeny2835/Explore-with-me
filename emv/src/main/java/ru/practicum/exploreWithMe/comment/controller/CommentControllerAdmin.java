package ru.practicum.exploreWithMe.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.exploreWithMe.comment.service.CommentServiceAdmin;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/admin/comments/{commentId}")
@RequiredArgsConstructor
@Slf4j
public class CommentControllerAdmin {
    private final CommentServiceAdmin commentServiceAdmin;

    @DeleteMapping
    public void delete(
            @PathVariable @Positive int commentId) {
        log.info("Delete comment with id={}", commentId);
        commentServiceAdmin.delete(commentId);
    }
}