package ru.practicum.exploreWithMe.comment.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.comment.dto.CommentFullDto;
import ru.practicum.exploreWithMe.comment.dto.CommentNewDto;
import ru.practicum.exploreWithMe.comment.model.Comment;

@Component
public class CommentMapper {
    public Comment toNewComment(CommentNewDto commentNewDto) {
        return Comment.builder()
                .text(commentNewDto.getText())
                .build();
    }

    public CommentFullDto toCommentFullDto(Comment comment) {
        return CommentFullDto.builder()
                .id(comment.getId())
                .event(comment.getEvent().getId())
                .author(comment.getAuthor().getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }
}