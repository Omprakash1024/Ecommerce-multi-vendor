package com.app.oneplace.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.oneplace.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
