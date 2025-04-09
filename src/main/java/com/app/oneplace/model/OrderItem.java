package com.app.oneplace.model;

import java.time.LocalDateTime;
import java.util.List;

import com.app.oneplace.domain.OrderStatus;
import com.app.oneplace.domain.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class OrderItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@JsonIgnore
	@ManyToOne
	private Order order;
	
	@ManyToOne
	private Product product;
	
	private String sizes; // size can be xl,s,m,l,xxl //selected by user
	
	private int quantity;
	
	private Integer mrpPrice;
	
	private Integer sellingPrice;
	
	private Long userId;
	

}
