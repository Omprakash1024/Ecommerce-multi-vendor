package com.app.oneplace.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.oneplace.exceptions.ProductException;
import com.app.oneplace.model.Product;
import com.app.oneplace.model.Seller;
import com.app.oneplace.request.CreateProductRequest;
import com.app.oneplace.services.ProductService;
import com.app.oneplace.services.SellerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sellers/products")
public class SellerProductController {

	private final ProductService productService;
	private final SellerService sellerService;

	@GetMapping()
	public ResponseEntity<List<Product>> getProductBySellerId(
			@RequestHeader("Authorization") String jwt) throws Exception {

		Seller seller = sellerService.getSellerProfile(jwt);
		List<Product> products = productService.getProductBySellerId(seller.getId());
		return new ResponseEntity<>(products, HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<Product> createProduct(
			@RequestBody CreateProductRequest req,
			@RequestHeader("Authorization") String jwt) throws Exception {

		Seller seller = sellerService.getSellerProfile(jwt);
		Product product = productService.createProduct(req, seller);
		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@DeleteMapping("/{productId}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) throws Exception {

		try {
			productService.deleteProduct(productId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (ProductException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@PutMapping("/{productId}")
	public ResponseEntity<Product> updateProduct(@PathVariable Long productId,
			@RequestBody Product product) throws ProductException {

		Product updatedproduct = productService.updateProduct(productId, product);
		return new ResponseEntity<>(updatedproduct, HttpStatus.OK);

	}

	// @GetMapping("/getProductByFilter")
	// public ResponseEntity<Page<Product>> getAllProducts(
	// @RequestParam Integer pageNumber,
	// @RequestBody ProductFilterRequest product){

	// Page<Product> page = productService.getAllProduct(
	// product.getCategory(),
	// product.getBrand(),
	// product.getColor(),
	// product.getSize(),
	// product.getMinPrice(),
	// product.getMaxPrice(),
	// product.getMinDiscount(),
	// product.getSort(),
	// product.getStock(),
	// pageNumber
	// );

	// return new ResponseEntity<>(page, HttpStatus.OK);
	// }

}
