package com.arturoo404.NewsPage.controller.templates.shop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shop/cart")
public class CartController {

    @GetMapping()
    public String cartDetail(){
        return "shop/cart/detail";
    }
}
