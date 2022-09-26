package com.arturoo404.NewsPage.service.shop;

import com.arturoo404.NewsPage.entity.shop.order.dto.OrderDto;
import com.arturoo404.NewsPage.entity.shop.order.dto.OrderUserDetailDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;

import java.text.ParseException;
import java.util.List;

public interface OrderService {
    Object makeOrder(OrderDto orderDto) throws ExistInDatabaseException, ParseException;

    List<OrderUserDetailDto> getUserOrderDetail(String email);
}
