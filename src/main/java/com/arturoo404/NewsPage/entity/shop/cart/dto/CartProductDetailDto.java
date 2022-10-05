package com.arturoo404.NewsPage.entity.shop.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartProductDetailDto {

    private Integer quantity;
    private Long id;
    private String name;
    private Double price;
}
