package com.app.oneplace.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.app.oneplace.exceptions.ProductException;
import com.app.oneplace.model.Category;
import com.app.oneplace.model.Order;
import com.app.oneplace.model.OrderItem;
import com.app.oneplace.model.Product;
import com.app.oneplace.model.Seller;
import com.app.oneplace.repo.CategoryRepository;
import com.app.oneplace.repo.ProductRepository;
import com.app.oneplace.request.CreateProductRequest;
import com.app.oneplace.request.ProductFilterRequest;
import com.app.oneplace.response.ProductPageResponse;
import com.app.oneplace.response.ProductResponseDto;
import com.app.oneplace.response.ReviewResponseDto;
import com.app.oneplace.services.ProductService;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final RedissonClient redissonClient;

	@CacheEvict(value = "productListCache", allEntries = true)
	@Override
	public Product createProduct(CreateProductRequest req, Seller seller) {

		Category category1 = categoryRepository.findByCategoryId(req.getCategory());

		if (category1 == null) {
			Category category = new Category();
			category.setCategoryId(req.getCategory());
			category.setCategoryLevel(1);
			category1 = categoryRepository.save(category);
		}
		Category category2 = categoryRepository.findByCategoryId(req.getCategory2());

		if (category2 == null) {
			Category category = new Category();
			category.setCategoryId(req.getCategory2());
			category.setCategoryLevel(2);
			category.setParentCategory(category1);
			category2 = categoryRepository.save(category);
		}

		Category category3 = categoryRepository.findByCategoryId(req.getCategory3());

		if (category3 == null) {
			Category category = new Category();
			category.setCategoryId(req.getCategory3());
			category.setCategoryLevel(3);
			category.setParentCategory(category2);
			category2 = categoryRepository.save(category);
		}
		int discountPercentage = calcuateDiscountPercentage(req.getMrpPrice(), req.getSellingPrice());

		Product product = new Product();
		product.setSeller(seller);
		product.setCategory(category3);
		product.setDescription(req.getDescription());
		product.setCreatedAt(LocalDateTime.now());
		product.setTitle(req.getTitle());
		product.setColor(req.getColor());
		product.setSellingPrice(req.getSellingPrice());
		product.setImages(req.getImages());
		product.setMrpPrice(req.getMrpPrice());
		product.setSizes(req.getSize());
		product.setDiscountPercentage(discountPercentage);

		return productRepository.save(product);
	}

	private int calcuateDiscountPercentage(int mrpPrice, int sellingPrice) {
		if (mrpPrice <= 0) {
			throw new IllegalArgumentException("MRP price must be greater than 0");
		}
		double discount = mrpPrice - sellingPrice;
		double discountPercentage = (discount / mrpPrice) * 100;
		return (int) discountPercentage;
	}

	@CacheEvict(value = "productListCache", allEntries = true)
	@Override
	public void deleteProduct(Long productId) throws ProductException {
		Product product = findProductById(productId);
		productRepository.delete(product);
	}

	@CacheEvict(value = "productListCache", allEntries = true)
	@Override
	public Product updateProduct(Long productId, Product product) throws ProductException {/////////////////////
		findProductById(productId);
		product.setId(productId);

		return productRepository.save(product);
	}

	// @Cacheable("findbyproductid")
	@Override
	public Product findProductById(Long productId) throws ProductException {

		return productRepository.findById(productId)
				.orElseThrow(() -> new ProductException("Product not found with id - " + productId));
	}

	@Override
	public List<Product> searchProducts(String query) {

		return productRepository.searchProduct(query);
	}

	@Cacheable(value = "productListCache", key = "T(java.util.Objects).hash(#filterRequest.category, #filterRequest.brand, #filterRequest.color, "
			+
			"#filterRequest.size, #filterRequest.minPrice, #filterRequest.maxPrice, #filterRequest.minDiscount, " +
			"#filterRequest.sort, #filterRequest.stock, #filterRequest.pageNumber)")
	@Override
	public ProductPageResponse getAllProduct(ProductFilterRequest filterRequest) {
		String category = filterRequest.getCategory();
		String brand = filterRequest.getBrand();
		String color = filterRequest.getColor();
		String size = filterRequest.getSize();
		Integer minPrice = filterRequest.getMinPrice();
		Integer maxPrice = filterRequest.getMaxPrice();
		Integer minDiscount = filterRequest.getMinDiscount();
		String sort = filterRequest.getSort();
		String stock = filterRequest.getStock();
		Integer pageNumber = filterRequest.getPageNumber();

		Specification<Product> spec = (root, query, criteriaBuilder) -> {
			List<Predicate> predicate = new ArrayList<>();

			if (category != null) {
				Join<Product, Category> categoryJoin = root.join("category");
				predicate.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), category));
			}

			if (color != null && !color.isEmpty()) {
				predicate.add(criteriaBuilder.equal(root.get("color"), color));
			}
			if (size != null && !size.isEmpty()) {
				predicate.add(criteriaBuilder.equal(root.get("size"), size));
			}
			if (minPrice != null) {
				predicate.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice));
			}
			if (maxPrice != null) {
				predicate.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice));
			}
			if (minDiscount != null) {
				predicate.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercentage"), minDiscount));
			}
			if (stock != null) {
				predicate.add(criteriaBuilder.equal(root.get("stock"), stock));
			}
			return criteriaBuilder.and(predicate.toArray(new Predicate[0]));
		};

		Pageable pageable;
		if (sort != null && !sort.isEmpty()) {
			switch (sort) {
				case "price_low":
					pageable = PageRequest.of((pageNumber != null) ? pageNumber : 0, 10,
							Sort.by("sellingPrice").ascending());
					break;
				case "price_high":
					pageable = PageRequest.of((pageNumber != null) ? pageNumber : 0, 10,
							Sort.by("sellingPrice").descending());
					break;
				default:
					pageable = PageRequest.of((pageNumber != null) ? pageNumber : 0, 10,
							Sort.unsorted());
					break;
			}
		} else {
			pageable = PageRequest.of((pageNumber != null) ? pageNumber : 0, 10,
					Sort.unsorted());
		}

		Page<Product> productPage = productRepository.findAll(spec, pageable);

		List<ProductResponseDto> productDtos = productPage.getContent().stream()
				.map(p -> new ProductResponseDto(
						p.getId(),
						p.getTitle(),
						p.getDescription(),
						p.getSellingPrice(),
						p.getMrpPrice(),
						p.getDiscountPercentage(),
						p.getQuantity(),
						p.getColor(),
						p.getImages() != null ? new ArrayList<>(p.getImages()) : List.of(),
						p.getNumRating(),
						p.getCategory() != null ? p.getCategory().getCategoryId() : null,
						p.getSeller() != null ? p.getSeller().getSellerName() : null,
						p.getSizes(),
						p.getReviews() != null ? p.getReviews().stream()
								.map(r -> new ReviewResponseDto(
										r.getId(),
										r.getReviewerName(),
										r.getReviewText(),
										r.getRating(),
										r.getProductImages() != null ? new ArrayList<>(r.getProductImages())
												: List.of(),
										r.getUser() != null ? r.getUser().getUsername() : null,
										r.getCreatedAt()))
								.toList() : List.of()

				))
				.toList();

		return new ProductPageResponse(
				productDtos,
				productPage.getNumber(),
				productPage.getTotalPages(),
				productPage.getTotalElements());
	}

	@Override
	public List<Product> getProductBySellerId(Long sellerId) {

		return productRepository.findBySellerId(sellerId);
	}

	@CacheEvict(value = "productListCache", allEntries = true)
	@Transactional
	public void restockProductsForCancelledOrder(Order order) {
		for (OrderItem item : order.getOrderItems()) {
			Product product = item.getProduct();
			RLock lock = redissonClient.getLock("lock:product:" + product.getId());

			try {
				if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {
					Product freshProduct = productRepository.findById(product.getId())
							.orElseThrow(() -> new RuntimeException("Product not found"));

					freshProduct.setQuantity(freshProduct.getQuantity() + item.getQuantity());
					productRepository.save(freshProduct);
				} else {
					throw new RuntimeException("Could not acquire lock to restock product: " + product.getTitle());
				}
			} catch (InterruptedException e) {
				throw new RuntimeException("Lock interrupted while restocking product: " + product.getTitle(), e);
			} finally {
				if (lock.isHeldByCurrentThread()) {
					lock.unlock();
				}
			}
		}
	}

	@Transactional
	public void restockProductsForCancelledOrder(Set<Order> orders) {
		for (Order order : orders) {
			for (OrderItem item : order.getOrderItems()) {
				Product product = item.getProduct();
				RLock lock = redissonClient.getLock("lock:product:" + product.getId());

				try {
					if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {
						Product freshProduct = productRepository.findById(product.getId())
								.orElseThrow(() -> new RuntimeException("Product not found"));

						freshProduct.setQuantity(freshProduct.getQuantity() + item.getQuantity());
						productRepository.save(freshProduct);
					} else {
						throw new RuntimeException("Could not acquire lock to restock product: " + product.getTitle());
					}
				} catch (InterruptedException e) {
					throw new RuntimeException("Lock interrupted while restocking product: " + product.getTitle(), e);
				} finally {
					if (lock.isHeldByCurrentThread()) {
						lock.unlock();
					}
				}
			}
		}

	}

}
