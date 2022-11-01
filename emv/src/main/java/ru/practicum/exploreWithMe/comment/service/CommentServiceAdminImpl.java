package ru.practicum.exploreWithMe.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.comment.model.Comment;
import ru.practicum.exploreWithMe.comment.repository.CommentRepository;
import ru.practicum.exploreWithMe.exception.exceptions.NotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceAdminImpl implements CommentServiceAdmin {
    private final CommentRepository commentRepository;

    public void delete(Integer commentId) {
        log.info("Delete comment with id={}", commentId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException(String.format("Comment not found, id=%d", commentId)));
        commentRepository.delete(comment);
    }
}