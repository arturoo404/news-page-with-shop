package com.arturoo404.NewsPage.entity.shop.product.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetail {

    private String name;
    private String description;
    private Double price;
    private Double discountPrice;
    private Integer productQuantity;
}
