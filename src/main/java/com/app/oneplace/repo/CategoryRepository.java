package com.app.oneplace.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.oneplace.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

	Category findByCategoryId(String categoryId);
}
