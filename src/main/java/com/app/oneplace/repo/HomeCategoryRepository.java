package com.app.oneplace.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.oneplace.model.HomeCategory;

@Repository
public interface HomeCategoryRepository extends JpaRepository<HomeCategory, Long> {

}
