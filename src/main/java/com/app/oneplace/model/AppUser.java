package com.app.oneplace.model;

import java.util.HashSet;
import java.util.Set;

import com.app.oneplace.domain.USER_ROLE;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
    @JsonProperty(access = Access.WRITE_ONLY)
	private String password;
    private String email;
	private String username;
	private String mobile;
	private USER_ROLE role =USER_ROLE.USER_CUSTOMER;
	@OneToMany
	private Set<Address> addresses = new HashSet<>();
	@ManyToMany
	@JsonIgnore
	private Set<Coupon> usedCoupens = new HashSet<>();
	
	
}
