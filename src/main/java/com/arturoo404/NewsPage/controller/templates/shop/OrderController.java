package com.arturoo404.NewsPage.controller.templates.shop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shop/order")
public class OrderController {

    @GetMapping()
    public String makeOrder(){
        return "shop/order/make-order";
    }
}
