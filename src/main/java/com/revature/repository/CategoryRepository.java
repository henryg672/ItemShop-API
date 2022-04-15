package com.revature.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{

}
