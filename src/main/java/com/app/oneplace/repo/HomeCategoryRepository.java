package com.app.oneplace.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.oneplace.model.HomeCategory;

public interface HomeCategoryRepository extends JpaRepository<HomeCategory, Long> {

	
}
