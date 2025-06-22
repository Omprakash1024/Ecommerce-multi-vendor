package com.app.oneplace.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.oneplace.exceptions.ProductException;
import com.app.oneplace.model.Product;
import com.app.oneplace.request.ProductFilterRequest;
import com.app.oneplace.response.ProductPageResponse;
import com.app.oneplace.services.ProductService;
import com.app.oneplace.services.RateLimiterService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final RateLimiterService rateLimiterService;

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) throws ProductException {
        Product product = productService.findProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam(required = false) String query)
            throws ProductException {
        List<Product> products = productService.searchProducts(query);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<ProductPageResponse> getAllProducts(HttpServletRequest request,
            @ModelAttribute ProductFilterRequest productFilterRequest) throws ProductException {

        String clientIp = request.getRemoteAddr();

        boolean allowed = rateLimiterService.tryConsumeToken("rate_limit:ip:" + clientIp, 5, 1);
        if (!allowed) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null);
        }

        ProductPageResponse productList = productService.getAllProduct(productFilterRequest);

        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

}
