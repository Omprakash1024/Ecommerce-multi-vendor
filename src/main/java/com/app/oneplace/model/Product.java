package com.app.oneplace.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String title;
	
	private String description;
	
	private int mrpPrice;
	
	private int sellingPrice;
	
	private int discountPercentage;
	
	private int quantity;
	
	private String color;
	
	@ElementCollection
	private  List<String> images = new ArrayList<>();
	
	private int numRating;
	
	@ManyToOne
	private Category category; //one category have Multiple products but one product have one category
	 
	@ManyToOne	
	private Seller seller;   //  one seller have multiple product
	
	private LocalDateTime createdAt;
	
	private String sizes;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL,orphanRemoval = true)
	private List<Review> reviews = new ArrayList<>();
	
	
}
