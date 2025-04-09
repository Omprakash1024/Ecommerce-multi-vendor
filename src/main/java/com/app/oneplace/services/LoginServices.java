package com.app.oneplace.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.app.oneplace.model.login;
import com.app.oneplace.repo.LoginRepository;

@Service
public class LoginServices {
	@ Autowired private LoginRepository loginRepository;
	public boolean validate(ModelAndView model, String userId, String password) {
		boolean isvalid =false;
		String ErrorMessage ="";
		Optional<login> lgList =loginRepository.findById(userId);
		String pass = lgList.get().getPassword();
		System.out.println("password :"+password +" pass from db :"+pass);
		if (pass.equals(password)) {
			ErrorMessage="Login Successful";
			isvalid = true;
		}else {
			ErrorMessage="User ID or Password is invalid";
		}
		model.addObject("ErrorMessage", ErrorMessage);
		return isvalid;
	}
	
	public boolean createUser(String userid, String pass, String username) {
		boolean isSuccess = false;
		login logn = new login();
		try {
			logn.setUserId(userid);
			logn.setPassword(pass);
			logn.setUserName(username);
			loginRepository.save(logn);
			isSuccess= true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return isSuccess;
	}
}
