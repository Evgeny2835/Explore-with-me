package ru.practicum.exploreWithMe.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.category.dto.CategoryDto;
import ru.practicum.exploreWithMe.category.service.CategoryServicePublic;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CategoryControllerPublic {
    private final CategoryServicePublic categoryServicePublic;

    @GetMapping
    public List<CategoryDto> getAll(
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info(String.format("Get categories, from=%d, size=%d", from, size));
        return categoryServicePublic.getAll(from, size);
    }

    @GetMapping("/{categoryId}")
    public CategoryDto getById(@PathVariable("categoryId") @Positive int categoryId) {
        log.info(String.format("Get category by id=%d", categoryId));
        return categoryServicePublic.getById(categoryId);
    }
}