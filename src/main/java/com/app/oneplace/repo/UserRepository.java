package com.app.oneplace.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.oneplace.model.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long>{

	public AppUser findByEmail(String email);
}
