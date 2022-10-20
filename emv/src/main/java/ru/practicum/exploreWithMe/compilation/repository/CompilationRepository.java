package ru.practicum.exploreWithMe.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exploreWithMe.compilation.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

    List<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);
}