package com.app.oneplace.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.app.oneplace.domain.HomeCategorySection;
import com.app.oneplace.model.Home;
import com.app.oneplace.model.Deal;
import com.app.oneplace.model.HomeCategory;
import com.app.oneplace.repo.DealRepository;
import com.app.oneplace.services.HomeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService{
	
	private final DealRepository dealRepository;

	@Override
	public Home createHomePageData(List<HomeCategory> allCategories) {
		
		List<HomeCategory> gridCategories = allCategories.stream()
				.filter(category -> category.getSection() == HomeCategorySection.GRID)
				.collect(Collectors.toList());
		
		List<HomeCategory> shopByCategories = allCategories.stream()
				.filter(category -> category.getSection() == HomeCategorySection.SHOP_BY_CATEGORIES)
				.collect(Collectors.toList());
		
		List<HomeCategory> electronicsCategories = allCategories.stream()
				.filter(category -> category.getSection() == HomeCategorySection.ELECTRIC_CATEGORIES)
				.collect(Collectors.toList());
		
		List<HomeCategory> dealCategories = allCategories.stream()
				.filter(category -> category.getSection() == HomeCategorySection.DEALS)
				.collect(Collectors.toList());
		
		List<Deal> createdDeals = new ArrayList<>();
		
		if(dealRepository.findAll().isEmpty()) {
			List<Deal> deals = allCategories.stream()
					.filter(category -> category.getSection() == HomeCategorySection.DEALS)
					.map(category -> new Deal(null,10,category))  //assuming discount 10% for all the deals
					.collect(Collectors.toList());
			
			createdDeals= dealRepository.saveAll(deals);
		}else{
			createdDeals = dealRepository.findAll();
		}
		
		Home home = new Home();
		
		home.setGrid(gridCategories);
		home.setShopByCategories(shopByCategories);
		home.setElectronicCategories(electronicsCategories);
		home.setDeals(createdDeals);
		home.setDealCategories(dealCategories);
		
		return home;
	}

}
