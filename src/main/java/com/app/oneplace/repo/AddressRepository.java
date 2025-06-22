package com.app.oneplace.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.oneplace.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
