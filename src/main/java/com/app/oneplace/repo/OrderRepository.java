package com.app.oneplace.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.oneplace.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUserId(Long userId);

	List<Order> findBySellerId(Long sellerId);
}
