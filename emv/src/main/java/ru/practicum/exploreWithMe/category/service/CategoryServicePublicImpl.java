package ru.practicum.exploreWithMe.category.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.category.dto.CategoryDto;
import ru.practicum.exploreWithMe.category.mapper.CategoryMapper;
import ru.practicum.exploreWithMe.category.model.Category;
import ru.practicum.exploreWithMe.category.repository.CategoryRepository;
import ru.practicum.exploreWithMe.exception.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CategoryServicePublicImpl implements CategoryServicePublic {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        log.info("Service: start processing a category selection request, from={}, size={}", from, size);
        PageRequest pageRequest = PageRequest.of(from, size, Sort.by("id"));
        return categoryRepository
                .findAll(pageRequest)
                .getContent()
                .stream()
                .map(categoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(Integer categoryId) {
        log.info("Service: start of processing the request to get a category, id={}", categoryId);
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException(String.format("Category not found, id=%d", categoryId)));
        return categoryMapper.toCategoryDto(category);
    }
}