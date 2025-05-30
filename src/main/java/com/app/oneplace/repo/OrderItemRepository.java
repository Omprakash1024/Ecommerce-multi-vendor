package com.app.oneplace.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.oneplace.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
