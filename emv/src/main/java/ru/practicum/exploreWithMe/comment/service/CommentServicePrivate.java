package ru.practicum.exploreWithMe.comment.service;

import ru.practicum.exploreWithMe.comment.dto.CommentFullDto;
import ru.practicum.exploreWithMe.comment.dto.CommentNewDto;
import ru.practicum.exploreWithMe.comment.dto.CommentUpdateDto;

public interface CommentServicePrivate {

    CommentFullDto create(Integer userId, CommentNewDto commentNewDto);

    CommentFullDto update(Integer userId, CommentUpdateDto commentUpdateDto);

    void delete(Integer userId, Integer commentId);
}