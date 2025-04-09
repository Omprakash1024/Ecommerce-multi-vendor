package com.app.oneplace.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.oneplace.model.login;

public interface LoginRepository extends JpaRepository<login, String>{

}
