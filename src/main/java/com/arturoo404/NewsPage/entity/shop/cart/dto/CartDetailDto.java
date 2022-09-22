package com.arturoo404.NewsPage.entity.shop.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CartDetailDto {

    private Double amount;
    private Long quantity;
    private List<CartProductDetailDto> product;
}
