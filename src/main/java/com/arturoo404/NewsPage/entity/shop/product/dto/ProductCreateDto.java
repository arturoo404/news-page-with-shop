package com.arturoo404.NewsPage.entity.shop.product.dto;

import com.arturoo404.NewsPage.enums.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductCreateDto {
    private String name;
    private Double price;
    private String description;
    private ProductCategory productCategory;
}
