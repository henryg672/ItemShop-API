package com.revature.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.model.Category;
import com.revature.repository.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	CategoryRepository categoryRepository;
	
	public void createCategory(Category category) {
		categoryRepository.save(category);
	}

	public List<Category> listCategory() {
		return categoryRepository.findAll();
	}
	
	public void editCategory(int categoryId, Category updateCategory) {
		Category category = categoryRepository.getById(categoryId);
		category.setCategoryName(updateCategory.getCategoryName());
		category.setDescription(updateCategory.getDescription());
		category.setImageUrl(category.getImageUrl());
		categoryRepository.save(category);
	}

	public boolean findById(int categoryId) {
		return categoryRepository.findById(categoryId).isPresent();
		}
}
