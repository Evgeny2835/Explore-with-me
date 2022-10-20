package ru.practicum.exploreWithMe.category.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.category.dto.CategoryDto;
import ru.practicum.exploreWithMe.category.dto.CategoryNewDto;
import ru.practicum.exploreWithMe.category.model.Category;

@Component
public class CategoryMapper {

    public Category toNewCategory(CategoryNewDto categoryNewDto) {
        return Category.builder()
                .name(categoryNewDto.getName())
                .build();
    }

    public CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}