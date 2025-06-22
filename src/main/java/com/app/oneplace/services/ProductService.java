package com.app.oneplace.services;

import java.util.List;
import java.util.Set;

//import org.springframework.data.domain.Page;

import com.app.oneplace.exceptions.ProductException;
import com.app.oneplace.model.Order;
import com.app.oneplace.model.Product;
import com.app.oneplace.model.Seller;
import com.app.oneplace.request.CreateProductRequest;
import com.app.oneplace.request.ProductFilterRequest;
import com.app.oneplace.response.ProductPageResponse;

public interface ProductService {

	public Product createProduct(CreateProductRequest req, Seller sellr);

	public void deleteProduct(Long productId) throws ProductException;

	public Product updateProduct(Long productId, Product product) throws ProductException;

	public Product findProductById(Long productId) throws ProductException;

	List<Product> searchProducts(String query);

	public ProductPageResponse getAllProduct(ProductFilterRequest productFilterRequest);

	public List<Product> getProductBySellerId(Long sellerId);

	public void restockProductsForCancelledOrder(Order order);

	public void restockProductsForCancelledOrder(Set<Order> order);
}
