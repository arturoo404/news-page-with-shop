package com.arturoo404.NewsPage.entity.shop.order.dto;

import com.arturoo404.NewsPage.entity.shop.order_detail.dto.OrderProductDto;
import com.arturoo404.NewsPage.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderDetailDto {

    private Date date;
    private OrderStatus orderStatus;
    private Double amount;
    List<OrderProductDto> products;
}
