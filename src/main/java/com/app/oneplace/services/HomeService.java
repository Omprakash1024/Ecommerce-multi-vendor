package com.app.oneplace.services;

import java.util.List;

import com.app.oneplace.model.Home;
import com.app.oneplace.model.HomeCategory;

public interface HomeService {

	public Home createHomePageData(List<HomeCategory> allCategories);
}
