package com.app.oneplace.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class login {
    @Id
 //   @GeneratedValue(strategy = GenerationType.AUTO)
	private String userId;
	private String userName;
	private String password;
}
