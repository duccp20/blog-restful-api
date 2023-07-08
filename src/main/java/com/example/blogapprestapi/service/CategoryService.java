package com.example.blogapprestapi.service;

import com.example.blogapprestapi.model.dto.request.CategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO addCategory(CategoryDTO categoryDto);

    CategoryDTO getCategory(Long categoryId);

    List<CategoryDTO> getAllCategories();

    CategoryDTO updateCategory(CategoryDTO categoryDto, Long categoryId);

    void deleteCategory(Long categoryId);
}
