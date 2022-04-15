/*-
 * https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html
 */
package com.revature.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revature.common.ApiResponse;
import com.revature.dto.ProductDto;
import com.revature.model.Category;
import com.revature.model.Product;
import com.revature.repository.CategoryRepository;
import com.revature.repository.ProductRepository;
import com.revature.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {
	
	
	@Autowired
	ProductService productService;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);
	
	
	@PostMapping("/create")
	public ResponseEntity<ApiResponse> createProduct(@RequestBody ProductDto productDto) {
		Optional<Category> optionalCategory = categoryRepository.findById(productDto.getCategoryId());
		if(!optionalCategory.isPresent()) {
			return new ResponseEntity<>(new ApiResponse(false, "Category does not exist"), HttpStatus.BAD_REQUEST);
		}
		productService.createProduct(productDto, optionalCategory.get());
		return new ResponseEntity<>(new ApiResponse(true, "Product has been added"), HttpStatus.CREATED);
	}
	
	@GetMapping("/list")
	public ResponseEntity<List<ProductDto>> getProducts() {
		MDC.put("requestId", UUID.randomUUID().toString());
		List<ProductDto> products = productService.getAllProducts();
		LOG.info("Products retrieved");
		return new ResponseEntity<>(products, HttpStatus.OK);
		
	}
	
//	@GetMapping("/list/{name}")
//	public ResponseEntity<List<Product>> getProductByName(@RequestParam String name) {
//		List<Product> productByName = productRepository.findByName(name);
//		return new ResponseEntity<>(productByName, HttpStatus.OK);
//	}
	
	@GetMapping("/list/{price}")
	public ResponseEntity<List<Product>> getProductByPrice(@RequestParam double price) {
		List<Product> productByPrice = productRepository.findByPrice(price);
		return new ResponseEntity<>(productByPrice, HttpStatus.OK);
	}
	
	@PutMapping("/update/{productId}")
	public ResponseEntity<ApiResponse> updateProduct(@PathVariable("productId") Integer productId, @RequestBody ProductDto productDto) throws Exception {
		Optional<Category> optionalCategory = categoryRepository.findById(productDto.getCategoryId());
		if(!optionalCategory.isPresent()) {
			return new ResponseEntity<>(new ApiResponse(false, "Category does not exist"), HttpStatus.BAD_REQUEST);
		}
		productService.updateProduct(productDto, productId);
		return new ResponseEntity<>(new ApiResponse(true, "Product has been updated"), HttpStatus.OK);
	}
}
