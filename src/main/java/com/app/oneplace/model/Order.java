package com.app.oneplace.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.app.oneplace.domain.OrderStatus;
import com.app.oneplace.domain.PaymentStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name ="orders")
public class Order {
 
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String orderId;
	
	@ManyToOne //one user can place multiple orders
	private AppUser user; 
	
	private Long sellerId; 
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL,orphanRemoval = true)
	private List<OrderItem> orderItems = new ArrayList<>();
	
	@ManyToOne   //one address can have multiple orders
	private Address shippingAddress; 
	
	@Embedded
	private PaymentDetails paymentDetails = new PaymentDetails();
	
	private double totalMrpPrice;
	
	private Integer totalSellingPrice;
	
	private Integer discount;
	
	private OrderStatus orderStatus;
	
	private int totalItem;
	
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    
    private LocalDateTime orderDate = LocalDateTime.now();
    
    private LocalDateTime deliverDate = orderDate.plusDays(7);  //order will be delivered in 7 days // can be parameterized 
}
