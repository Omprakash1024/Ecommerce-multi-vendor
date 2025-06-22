package com.app.oneplace.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.oneplace.model.VerificationCode;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

	VerificationCode findByEmail(String email);

	VerificationCode findByOtp(String otp);
}
