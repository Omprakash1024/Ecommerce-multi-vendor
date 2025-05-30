package com.app.oneplace.services;

import java.util.List;
import java.util.Set;

import com.app.oneplace.domain.OrderStatus;
import com.app.oneplace.model.Address;
import com.app.oneplace.model.AppUser;
import com.app.oneplace.model.Cart;
import com.app.oneplace.model.Order;
import com.app.oneplace.model.OrderItem;

public interface OrderService {

	Set<Order> createOrder (AppUser user, Address shippingAddress, Cart cart);
	Order findOrderById(Long id)  throws Exception;
	List<Order> userOrderHistory(Long UserId);
	List<Order> sellersOrder(Long sellerId);
	Order updateOrderStatus(Long id, OrderStatus orderStatus)  throws Exception ;
	Order cancelOrder(Long orderId, AppUser user)throws Exception;
	OrderItem findOrderItemById(Long id)throws Exception;
}
