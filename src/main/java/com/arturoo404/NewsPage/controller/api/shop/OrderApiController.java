package com.arturoo404.NewsPage.controller.api.shop;

import com.arturoo404.NewsPage.entity.shop.order.dto.OrderDto;
import com.arturoo404.NewsPage.entity.shop.order.dto.OrderUserListDto;
import com.arturoo404.NewsPage.enums.OrderStatus;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.service.shop.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//TODO Bigdec price
@Controller
@RequestMapping(path = "/api/shop/order")
public class OrderApiController {

    private final OrderService orderService;

    @Autowired
    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("isAuthenticated()")
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/user-list")
    public ResponseEntity<?> userOrderList(@RequestParam("email") String email){
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getUserOrderList(email));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/user-detail")
    public ResponseEntity<?> userOrderDetail(@RequestParam("email") String email,
                                             @RequestParam("orderId") Long id){
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(orderService.getUserOrderDetail(email, id));
        } catch (ExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOY')")
    @GetMapping(path = "/management/list")
    public ResponseEntity<Page<OrderUserListDto>> management(@RequestParam("page") Integer page,
                                                             @RequestParam("status") OrderStatus orderStatus){
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getPageOfOrderList(page, orderStatus));
    }

    @PatchMapping(path = "/management/change-status")
    public ResponseEntity<?> changeOrderStatus(@RequestParam("orderId") Long orderId,
                                               @RequestParam("status") OrderStatus orderStatus){
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.updateStatus(orderId, orderStatus));
    }
}
