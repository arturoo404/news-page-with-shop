package com.arturoo404.NewsPage.entity.shop.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartNavInfoDto {

    private Double amount;
    private Long quantity;
}
