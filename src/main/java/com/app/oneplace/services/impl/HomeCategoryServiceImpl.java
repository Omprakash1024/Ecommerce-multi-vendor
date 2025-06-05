package com.app.oneplace.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.oneplace.model.HomeCategory;
import com.app.oneplace.repo.HomeCategoryRepository;
import com.app.oneplace.services.HomeCategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeCategoryServiceImpl implements HomeCategoryService{

	
	private final HomeCategoryRepository homeCategoryRepository;
	
	@Override
	public HomeCategory createHomeCategory(HomeCategory homeCategory) {
		
		return homeCategoryRepository.save(homeCategory);
	}

	@Override
	public List<HomeCategory> createCategories(List<HomeCategory> homeCategories) {
		if(homeCategoryRepository.findAll().isEmpty()) {
			homeCategoryRepository.saveAll(homeCategories);
		}
		
		return homeCategoryRepository.findAll();
	}

	@Override
	public HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id) throws Exception {
		
		HomeCategory existingCategory = homeCategoryRepository.findById(id)
				.orElseThrow(()-> new Exception("Category not found..."));
		
		if(homeCategory.getImage() !=null) {
			existingCategory.setImage(homeCategory.getImage());
		}
		
		if(homeCategory.getCategoryId() != null) {
			existingCategory.setCategoryId(homeCategory.getCategoryId());
		}
		
		return homeCategoryRepository.save(existingCategory);
	}

	@Override
	public List<HomeCategory> getallHomeCategories() {
		
		return homeCategoryRepository.findAll();
	}

}
