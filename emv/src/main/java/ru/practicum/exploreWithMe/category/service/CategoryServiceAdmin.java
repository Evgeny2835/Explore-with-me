package ru.practicum.exploreWithMe.category.service;

import ru.practicum.exploreWithMe.category.dto.CategoryDto;
import ru.practicum.exploreWithMe.category.dto.CategoryNewDto;
import ru.practicum.exploreWithMe.category.model.Category;

public interface CategoryServiceAdmin {

    CategoryDto create(CategoryNewDto categoryNewDto);

    CategoryDto update(CategoryDto categoryDto);

    void delete(Integer categoryId);

    Category validateCategoryByIdAndReturnCategory(Integer categoryId);
}