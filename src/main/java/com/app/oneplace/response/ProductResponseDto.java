package com.app.oneplace.response;

import java.util.List;

import com.app.oneplace.model.Category;
import com.app.oneplace.model.Review;
import com.app.oneplace.model.Seller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String title;
    private String description;
    private Integer sellingPrice;
    private Integer mrpPrice;
    // Add other fields you want to expose
    private int discountPercentage;

    private int quantity;

    private String color;

    private List<String> images;

    private int numRating;

    private String categoryId; // corrected from int to String
    private String sellerName; // corrected from int to String
    private String sizes;

    private List<ReviewResponseDto> reviews;
}
