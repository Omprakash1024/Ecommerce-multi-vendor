package com.app.oneplace.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPageResponse {
    private List<ProductResponseDto> content;
    private int pageNumber;
    private int totalPages;
    private long totalElements;
}
