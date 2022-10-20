package ru.practicum.exploreWithMe.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.exploreWithMe.event.model.Event;
import ru.practicum.exploreWithMe.event.model.EventState;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventJpaRepository extends JpaRepository<Event, Integer> {

    @Query("select i from Event i " +
            "where i.initiator.id in ?1 " +
            "and i.state in ?2 " +
            "and i.category.id in ?3 " +
            "and i.eventDate between ?4 and ?5")
    List<Event> findByParameters(List<Integer> users,
                                 List<EventState> states,
                                 List<Integer> categories,
                                 LocalDateTime start,
                                 LocalDateTime end,
                                 Pageable pageable);

    List<Event> findAllByIdIn(Collection<Integer> ids, Pageable pageable);

    List<Event> findAllByInitiatorId(Integer userId, Pageable pageable);
}