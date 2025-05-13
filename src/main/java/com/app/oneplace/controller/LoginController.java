 package com.app.oneplace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.app.oneplace.services.LoginServices;

@RestController
@RequestMapping("/login.htm")
public class LoginController {
	@Autowired
	private LoginServices loginServices;

	@GetMapping
	public ModelAndView loginView() {
		ModelAndView model = new ModelAndView();
		model.setViewName("login/login");
		return model;
	}

	@PostMapping("/login")
	public ModelAndView checkLogin(@RequestParam String userId, @RequestParam String password) {
		ModelAndView model = new ModelAndView();
		System.out.println("userId :" + userId + "pass :" + password);
		if (loginServices.validate(model, userId, password)) {
			model.setViewName("homepage/home");
		} else {
			model.setViewName("login/login");
		}

		return model;
	}

	@PostMapping("/createAccountSave")
	public ModelAndView createAccount(@RequestParam String userId, @RequestParam String password,
			@RequestParam String confPass, @RequestParam String userName) {
		System.out.println(
				"userId :" + userId + "pass :" + password + " conf pass :" + confPass + " userName :" + userId);
		ModelAndView model = new ModelAndView();
		if (!password.equals(confPass)) {
			model.addObject("ErrorMessage", "Password is mismatching");
		}

		if (loginServices.createUser(userId, confPass, userName)) {
			model.addObject("message", "Account created successfully");
		} else {
			model.addObject("ErrorMessage", "Something went wrong on server...");
		}
		model.setViewName("login/login");

		return model;
	}

	@GetMapping("/createAccount")
	public ModelAndView signInView() {
		ModelAndView model = new ModelAndView();
		model.setViewName("login/signupview");
		return model;
	}
}
