package com.app.oneplace.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.oneplace.domain.AccountStatus;
import com.app.oneplace.model.Seller;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

	Seller findByEmail(String email);

	List<Seller> findByAccountStatus(AccountStatus status);
}
