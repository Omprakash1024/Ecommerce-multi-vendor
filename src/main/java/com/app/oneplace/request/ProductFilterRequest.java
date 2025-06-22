package com.app.oneplace.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ProductFilterRequest {
    private String category;
    private String brand;
    private String color;
    private String size;
    @Min(0)
    private Integer minPrice;
    private Integer maxPrice;
    private Integer minDiscount;
    private String sort;
    private String stock;
    private Integer pageNumber = 0;
}