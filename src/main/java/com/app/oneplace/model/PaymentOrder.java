package com.app.oneplace.model;

import java.util.HashSet;
import java.util.Set;

import com.app.oneplace.domain.PaymentMethod;
import com.app.oneplace.domain.PaymentOrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class PaymentOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Enumerated(EnumType.STRING)
	private PaymentOrderStatus status = PaymentOrderStatus.PENDING;

	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;

	@Column(name = "plid")
	private String paymentLinkId;

	private Long amount;

	@ManyToOne
	private AppUser user; // one user can pay using multiple payment method

	@OneToMany // if customer ordered product from different seller or brand that time need to
				// // create different orders
	private Set<Order> orders = new HashSet<Order>();
}
