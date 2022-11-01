package ru.practicum.exploreWithMe.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.comment.dto.CommentFullDto;
import ru.practicum.exploreWithMe.comment.dto.CommentNewDto;
import ru.practicum.exploreWithMe.comment.dto.CommentUpdateDto;
import ru.practicum.exploreWithMe.comment.mapper.CommentMapper;
import ru.practicum.exploreWithMe.comment.model.Comment;
import ru.practicum.exploreWithMe.comment.repository.CommentRepository;
import ru.practicum.exploreWithMe.event.model.Event;
import ru.practicum.exploreWithMe.event.repository.EventJpaRepository;
import ru.practicum.exploreWithMe.exception.exceptions.ConditionException;
import ru.practicum.exploreWithMe.exception.exceptions.NotFoundException;
import ru.practicum.exploreWithMe.user.model.User;
import ru.practicum.exploreWithMe.user.service.UserService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServicePrivateImpl implements CommentServicePrivate {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final EventJpaRepository eventJpaRepository;
    private final UserService userService;

    @Override
    public CommentFullDto create(Integer userId, CommentNewDto commentNewDto) {
        log.info("Create comment with parameters: userId={}, data={}",
                userId, commentNewDto);

        User user = userService.get(userId);
        Event event = getEventById(commentNewDto.getEventId());
        Comment comment = commentMapper.toNewComment(commentNewDto);

        comment.setEvent(event);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());

        return commentMapper.toCommentFullDto(commentRepository.save(comment));
    }

    @Override
    public CommentFullDto update(Integer userId, CommentUpdateDto commentUpdateDto) {
        log.info("Update comment with parameters: userId={}, data={}",
                userId, commentUpdateDto);

        User user = userService.get(userId);
        Comment comment = getCommentById(commentUpdateDto.getCommentId());
        validateUserIsCommentAuthor(user, comment);

        comment.setText(commentUpdateDto.getText());

        return commentMapper.toCommentFullDto(commentRepository.save(comment));
    }

    @Override
    public void delete(Integer userId, Integer commentId) {
        log.info("Delete comment with parameters: userId={}, commentId={}",
                userId, commentId);

        User user = userService.get(userId);
        Comment comment = getCommentById(commentId);
        validateUserIsCommentAuthor(user, comment);

        commentRepository.deleteById(commentId);
    }

    private Event getEventById(Integer eventId) {
        return eventJpaRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event not found, id=%d", eventId)));
    }

    private Comment getCommentById(Integer commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException(String.format("Comment not found, id=%d", commentId)));
    }

    private void validateUserIsCommentAuthor(User user, Comment comment) {
        if (!user.getId().equals(comment.getAuthor().getId())) {
            throw new ConditionException(String.format("Only the author can edit the comment, " +
                    "userId=%d, authorId=%d", user.getId(), comment.getAuthor().getId()));
        }
    }
}