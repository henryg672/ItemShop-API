package com.revature.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
	
//	List<Product> findByName(String name);
	
	List<Product> findByPrice(double price);
}
