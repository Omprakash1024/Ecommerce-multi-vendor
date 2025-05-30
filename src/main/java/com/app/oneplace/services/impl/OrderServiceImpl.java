package com.app.oneplace.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.app.oneplace.domain.OrderStatus;
import com.app.oneplace.domain.PaymentStatus;
import com.app.oneplace.model.Address;
import com.app.oneplace.model.AppUser;
import com.app.oneplace.model.Cart;
import com.app.oneplace.model.CartItem;
import com.app.oneplace.model.Order;
import com.app.oneplace.model.OrderItem;
import com.app.oneplace.repo.AddressRepository;
import com.app.oneplace.repo.OrderItemRepository;
import com.app.oneplace.repo.OrderRepository;
import com.app.oneplace.services.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

	private final OrderRepository orderRepository;
	private final AddressRepository addressRepository;
	private final OrderItemRepository orderItemRepository;
	@Override
	public Set<Order> createOrder(AppUser user, Address shippingAddress, Cart cart) {
		if(!user.getAddresses().contains(shippingAddress)) {
			user.getAddresses().add(shippingAddress);
		}
		
		Address address = addressRepository.save(shippingAddress);
		
		Map<Long, List<CartItem>> itemsBySeller = cart.getCartItems().stream().
				collect(Collectors.groupingBy(item->item.getProduct().getSeller().getId()));  //grouping cart items by seller 
		//eg ->4 items -> seller id 1
		//     2 items -> seller id 2
		//     1 items -> seller id 3   //-> each seller item groups considered as separate order   -> hence for each seller need to created orders 
		Set<Order> orders = new HashSet<>();
		
		for(Map.Entry<Long , List<CartItem>> entry : itemsBySeller.entrySet()) {
			
			Long sellerId = entry.getKey();  //eg -> seller 1
			
			List<CartItem> items = entry.getValue(); // List of items -> [shirt, t-shirt, Kurta]
			
			int totalOrderSellingPrice = items.stream().mapToInt(
					CartItem::getSellingPrice
					).sum();
			
			int totalOrderMrpPrice = items.stream().mapToInt(
					CartItem::getMrpPrice
					).sum();
			
			int totalItem = items.stream().mapToInt(CartItem::getQuantity).sum();
			
			Order createdOrder= new Order();
			createdOrder.setUser(user);
			createdOrder.setSellerId(sellerId);
			createdOrder.setTotalMrpPrice(totalOrderMrpPrice);
			createdOrder.setTotalSellingPrice(totalOrderSellingPrice);
			createdOrder.setTotalItem(totalItem);
			createdOrder.setShippingAddress(address);
			createdOrder.setOrderStatus(OrderStatus.PENDING);
			createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);
			
			Order savedOrder = orderRepository.save(createdOrder);
			orders.add(savedOrder);
			
			List<OrderItem> orderItems= new ArrayList<>();
			
			for(CartItem item: items) {
				OrderItem orderItem = new OrderItem();
				orderItem.setOrder(savedOrder);
				orderItem.setMrpPrice(item.getMrpPrice());
				orderItem.setProduct(item.getProduct());
				orderItem.setQuantity(item.getQuantity());
				orderItem.setSizes(item.getSize());
				orderItem.setUserId(item.getUserId());
				orderItem.setSellingPrice(item.getSellingPrice());
				
				savedOrder.getOrderItems().add(orderItem);
				
				OrderItem savedOrderItem = orderItemRepository.save(orderItem);
				orderItems.add(savedOrderItem);
			}
		}
		
		return orders;
	}

	@Override
	public Order findOrderById(Long id) throws Exception {
		
		return orderRepository.findById(id).orElseThrow(()-> new Exception("Order not found..."));
	}

	@Override
	public List<Order> userOrderHistory(Long UserId) {
		
		return orderRepository.findByUserId(UserId);
	}

	@Override
	public List<Order> sellersOrder(Long sellerId) {
		
		return orderRepository.findBySellerId(sellerId);
	}

	@Override
	public Order updateOrderStatus(Long id, OrderStatus orderStatus) throws Exception {
		Order order = findOrderById(id);
		order.setOrderStatus(orderStatus);
		return orderRepository.save(order);
	}

	@Override
	public Order cancelOrder(Long orderId, AppUser user) throws Exception {
		Order order = findOrderById(orderId);
		
		if(!order.getUser().getId().equals(user.getId())) {
			throw new Exception("You don't have access to this order");
		}
		order.setOrderStatus(OrderStatus.CANCELLED);
		return orderRepository.save(order);
	}

	@Override
	public OrderItem findOrderItemById(Long id) throws Exception {
		
		return orderItemRepository.findById(id).orElseThrow(()-> new Exception("Order item not exists..."));
	}

}
