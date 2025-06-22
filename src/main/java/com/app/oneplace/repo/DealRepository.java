package com.app.oneplace.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.oneplace.model.Deal;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {

}
