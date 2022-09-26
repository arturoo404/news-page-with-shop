package com.arturoo404.NewsPage.entity.shop.order.dto;

import com.arturoo404.NewsPage.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class OrderUserListDto {

    private Long id;
    private Date date;
    private OrderStatus orderStatus;
    private Double amount;
}
