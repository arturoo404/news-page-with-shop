package com.arturoo404.NewsPage.controller.templates.shop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/shop/product")
public class Product {

    @GetMapping(path = "/list")
    public String productList(){
        return "shop/product/list";
    }

    @GetMapping(path = "/category-list")
    public String productList(@RequestParam("category") String category){
        return "shop/product/category-list";
    }

    @GetMapping(path = "/add-product")
    public String addProduct(){
        return "shop/product/add-product";
    }

    @GetMapping(path = "/detail")
    public String productDetail(@RequestParam("id") Long id){
        return "shop/product/detail";
    }

}
