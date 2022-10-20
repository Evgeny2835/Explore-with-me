package ru.practicum.exploreWithMe.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exploreWithMe.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {

    Optional<Request> findByRequesterIdAndEventId(Integer userId, Integer eventId);

    List<Request> findByEventId(Integer event);

    List<Request> findAllByRequesterId(Integer userId);

    List<Request> findAllByEventId(Integer eventId);
}