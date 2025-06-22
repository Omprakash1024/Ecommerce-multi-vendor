package com.app.oneplace.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDto {
    private Long id;
    private String reviewerName;
    private String reviewText;
    private double rating;
    private List<String> productImages;
    private String userName; // Or userId if you prefer
    private LocalDateTime createdAt;
}
