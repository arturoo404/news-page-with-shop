package com.arturoo404.NewsPage.service.shop;

import com.arturoo404.NewsPage.entity.shop.order.dto.OrderDetailDto;
import com.arturoo404.NewsPage.entity.shop.order.dto.OrderDto;
import com.arturoo404.NewsPage.entity.shop.order.dto.OrderUserListDto;
import com.arturoo404.NewsPage.enums.OrderStatus;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.List;

public interface OrderService {
    Object makeOrder(OrderDto orderDto) throws ExistInDatabaseException, ParseException;

    List<OrderUserListDto> getUserOrderList(String email);

    OrderDetailDto getUserOrderDetail(String email, Long id) throws ExistInDatabaseException;

    Page<OrderUserListDto> getPageOfOrderList(Integer page, OrderStatus orderStatus);
}
