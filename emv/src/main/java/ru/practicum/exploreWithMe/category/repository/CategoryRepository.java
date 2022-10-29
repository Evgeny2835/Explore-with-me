package ru.practicum.exploreWithMe.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exploreWithMe.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}