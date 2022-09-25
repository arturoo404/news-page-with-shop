package com.arturoo404.NewsPage.controller.api.shop;

import com.arturoo404.NewsPage.entity.shop.order.dto.OrderDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.service.shop.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/api/shop/order")
public class OrderApiController {

    private final OrderService orderService;

    @Autowired
    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(path = "/make")
    public ResponseEntity<?> makeOrder(@RequestBody OrderDto orderDto){
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(orderService.makeOrder(orderDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
