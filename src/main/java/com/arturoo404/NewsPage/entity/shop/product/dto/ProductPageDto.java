package com.arturoo404.NewsPage.entity.shop.product.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPageDto {
    private Long id;
    private Double price;
    private Double discountPrice;
    private String name;
}
