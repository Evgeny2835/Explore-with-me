package ru.practicum.exploreWithMe.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.category.dto.CategoryDto;
import ru.practicum.exploreWithMe.category.dto.CategoryNewDto;
import ru.practicum.exploreWithMe.category.service.CategoryServiceAdmin;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Validated
public class CategoryControllerAdmin {
    private final CategoryServiceAdmin categoryServiceAdmin;

    @PostMapping
    public CategoryDto create(
            @RequestBody @Valid CategoryNewDto categoryNewDto) {
        return categoryServiceAdmin.create(categoryNewDto);
    }

    @PatchMapping
    public CategoryDto update(
            @RequestBody @Valid CategoryDto categoryDto) {
        return categoryServiceAdmin.update(categoryDto);
    }

    @DeleteMapping("/{categoryId}")
    public void delete(
            @PathVariable("categoryId") @Positive int categoryId) {
        categoryServiceAdmin.delete(categoryId);
    }
}