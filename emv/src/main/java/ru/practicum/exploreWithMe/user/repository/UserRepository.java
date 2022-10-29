package ru.practicum.exploreWithMe.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exploreWithMe.user.model.User;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findAllByIdIn(Collection<Integer> ids, Pageable pageable);
}