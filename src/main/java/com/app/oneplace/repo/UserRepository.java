package com.app.oneplace.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.oneplace.model.AppUser;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

	public AppUser findByEmail(String email);
}
