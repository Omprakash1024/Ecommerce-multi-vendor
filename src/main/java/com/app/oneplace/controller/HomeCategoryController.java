package com.app.oneplace.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.oneplace.model.Home;
import com.app.oneplace.model.HomeCategory;
import com.app.oneplace.services.HomeCategoryService;
import com.app.oneplace.services.HomeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HomeCategoryController {

	private final HomeCategoryService homeCategoryService;
	private final HomeService homeService;
	
	@PostMapping("/home/categories")
	public ResponseEntity<Home> createHomeCategories(
			@RequestBody List<HomeCategory> homeCategories
			){
		
		List<HomeCategory> categories = homeCategoryService.createCategories(homeCategories);
		Home home = homeService.createHomePageData(categories);
		
		return new ResponseEntity<Home>(home,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/admin/home-category")
	public ResponseEntity<List<HomeCategory>> getHomeCategory(){
		
		List<HomeCategory> categories= homeCategoryService.getallHomeCategories(); 
		
		return new ResponseEntity<List<HomeCategory>>(categories,HttpStatus.OK);
	 
	}
	
	@GetMapping("/admin/home-category/{id}")
	public ResponseEntity<HomeCategory> updateHomeCategory(
			@PathVariable Long id,
			@RequestBody HomeCategory homeCategory) throws Exception{
		
	  HomeCategory updatedCategory =homeCategoryService.updateHomeCategory(homeCategory, id);
	  
		
		return new ResponseEntity<>(updatedCategory,HttpStatus.OK);
	 
	}
}
