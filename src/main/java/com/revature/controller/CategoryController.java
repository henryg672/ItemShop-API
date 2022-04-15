/*-
 *  https://www.baeldung.com/spring-response-entity
 */
package com.revature.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.common.ApiResponse;
import com.revature.model.Category;
import com.revature.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {

	@Autowired
	CategoryService categoryService;
	
	@PostMapping("/create")
	public ResponseEntity<ApiResponse> createCategory(@RequestBody Category category) {
		categoryService.createCategory(category);
		return new ResponseEntity<>(new ApiResponse(true, "Category created"), HttpStatus.CREATED);
	}
	
	@GetMapping("/list")
	public List<Category> listCategory() {
		categoryService.listCategory();
		return categoryService.listCategory();
	}
	
	@PutMapping("/update/{categoryId}")
	public ResponseEntity<ApiResponse> updateCategory(@PathVariable("categoryId") int categoryId, @RequestBody Category category) {
		if(!categoryService.findById(categoryId)) {
			return new ResponseEntity<>(new ApiResponse(false, "Category does not exist"), HttpStatus.NOT_FOUND);
		}
		categoryService.editCategory(categoryId, category);
		return new ResponseEntity<>(new ApiResponse(true, "Category updated"), HttpStatus.OK);
	}
}
