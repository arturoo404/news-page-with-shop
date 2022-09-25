package com.arturoo404.NewsPage.service.shop;

import com.arturoo404.NewsPage.entity.shop.order.dto.OrderDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;

import java.text.ParseException;

public interface OrderService {
    Object makeOrder(OrderDto orderDto) throws ExistInDatabaseException, ParseException;
}
