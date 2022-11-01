package ru.practicum.exploreWithMe.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exploreWithMe.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
