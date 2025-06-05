package com.app.oneplace.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.oneplace.model.Deal;

public interface DealRepository extends JpaRepository<Deal, Long>{

}
