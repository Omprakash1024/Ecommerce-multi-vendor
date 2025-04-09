package com.app.oneplace.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
public class Cart {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@OneToOne
	private AppUser user;
	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL,orphanRemoval = true) //if user deleted the item from the cartItems, here also it will update
	private Set<CartItem> cartItems = new HashSet<>();
	
	private double totalSellingPrice;
	
	private int totalItem;
	
	private int totalMrpPrice;
	
	private int discount;
	
	private String couponCode;

}
