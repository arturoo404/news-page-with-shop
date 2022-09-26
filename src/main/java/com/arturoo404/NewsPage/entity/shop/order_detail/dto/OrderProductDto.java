package com.arturoo404.NewsPage.entity.shop.order_detail.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductDto {

    private Double price;
    private Long quantity;
}
