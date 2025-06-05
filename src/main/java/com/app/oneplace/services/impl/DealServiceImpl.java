package com.app.oneplace.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.oneplace.model.Deal;
import com.app.oneplace.model.HomeCategory;
import com.app.oneplace.repo.DealRepository;
import com.app.oneplace.repo.HomeCategoryRepository;
import com.app.oneplace.services.DealService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService{

	private final DealRepository dealRepository;
	private final HomeCategoryRepository homeCategoryRepository;
	
	@Override
	public List<Deal> getDeals() {
		
		return dealRepository.findAll();
	}

	@Override
	public Deal createDeal(Deal deal) {
		HomeCategory homeCategory= homeCategoryRepository.findById(deal.getCategory().getId()).orElse(null);
		
		Deal newDeal = dealRepository.save(deal);
		newDeal.setCategory(homeCategory);
		newDeal.setDiscount(deal.getDiscount());
		return dealRepository.save(newDeal);
	}

	@Override
	public Deal updateDeal(Deal deal,Long id) throws Exception {
		
		Deal existingDeal = dealRepository.findById(id).orElse(null);
		
		HomeCategory category = homeCategoryRepository.findById(deal.getCategory().getId()).orElse(null);
		
		if(existingDeal != null) {
			
			if(deal.getDiscount() !=null) {
				existingDeal.setDiscount(deal.getDiscount());
			}
			if(category !=null) {
				existingDeal.setCategory(category);
			}
			
			return dealRepository.save(existingDeal);
			
		}
		
		throw new Exception("Deal not dound");
	}

	@Override
	public void deleteDeal(Long id) throws Exception {
		Deal deal = dealRepository.findById(id)
				.orElseThrow(()-> new Exception("Deal not found..."));
		
		dealRepository.delete(deal);
		
		
	}

}
