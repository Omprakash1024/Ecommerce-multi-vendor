package com.app.oneplace.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.app.oneplace.exceptions.ProductException;
import com.app.oneplace.model.Product;
import com.app.oneplace.model.Seller;
import com.app.oneplace.request.CreateProductRequest;

public interface ProductService {

	public Product createProduct(CreateProductRequest req, Seller sellr);
	public void deleteProduct(Long productId) throws ProductException;
	public Product updateProduct(Long productId, Product product) throws ProductException;
	public Product findProductById(Long productId) throws ProductException;
	List<Product> searchProducts(String query);
	public Page<Product> getAllProduct(
			String category,
			String brand,
			String color,
			String size,
			Integer minPrice,
			Integer maxPrice,
			Integer minDiscount,
			String sort,
			String stock,
			Integer pageNumber
			);
	public List<Product> getProductBySellerId(Long sellerId);

}
