package ru.practicum.exploreWithMe.category.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.category.dto.CategoryDto;
import ru.practicum.exploreWithMe.category.dto.CategoryNewDto;
import ru.practicum.exploreWithMe.category.mapper.CategoryMapper;
import ru.practicum.exploreWithMe.category.model.Category;
import ru.practicum.exploreWithMe.category.repository.CategoryRepository;
import ru.practicum.exploreWithMe.exception.exceptions.ConflictException;
import ru.practicum.exploreWithMe.exception.exceptions.NotFoundException;

@Service
@AllArgsConstructor
@Slf4j
public class CategoryServiceAdminImpl implements CategoryServiceAdmin {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto create(CategoryNewDto categoryNewDto) {
        log.info("Service: start of processing a request to add a category in the service, name={}",
                categoryNewDto.getName());
        try {
            Category category = categoryMapper.toNewCategory(categoryNewDto);
            return categoryMapper.toCategoryDto(categoryRepository.save(category));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(
                    String.format("Category name is already in use, name=%s", categoryNewDto.getName()));
        }
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        log.info("Service: start of processing a request to update a category in the service, id={}, name={}",
                categoryDto.getId(), categoryDto.getName());
        Category category = validateCategoryByIdAndReturnCategory(categoryDto.getId());
        try {
            category.setName(categoryDto.getName());
            return categoryMapper.toCategoryDto(categoryRepository.save(category));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(
                    String.format("Category name is already in use, name=%s", categoryDto.getName()));
        }
    }

    @Override
    public void delete(Integer categoryId) {
        log.info("Service: start of processing a request to delete a category in the service, id={}", categoryId);
        try {
            categoryRepository.deleteById(categoryId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Category not found: id=%d", categoryId));
        }
    }

    @Override
    public Category validateCategoryByIdAndReturnCategory(Integer categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException(String.format("Category id not found, id=%d", categoryId)));
    }
}