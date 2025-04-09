package com.app.oneplace.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
public class Coupan {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String code;
	
	private double discountPercentage;
	
	private LocalDate validityStartDate;
	
	private LocalDate validityEndDate;
	
	private double minimumOrderValue;
	
	private boolean isActive =true;
	
	@ManyToMany(mappedBy = "usedCoupens")
	private Set<AppUser> usedByUsers= new HashSet<>();
}

