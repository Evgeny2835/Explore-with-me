package ru.practicum.exploreWithMe.category.service;

import ru.practicum.exploreWithMe.category.dto.CategoryDto;

import java.util.List;

public interface CategoryServicePublic {

    List<CategoryDto> getAll(Integer from, Integer size);

    CategoryDto getById(Integer categoryId);
}