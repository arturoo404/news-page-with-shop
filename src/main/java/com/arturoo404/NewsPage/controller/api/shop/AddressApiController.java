package com.arturoo404.NewsPage.controller.api.shop;

import com.arturoo404.NewsPage.service.shop.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/api/shop/address")
public class AddressApiController {


    private final AddressService addressService;

    @Autowired
    public AddressApiController(AddressService addressService) {
        this.addressService = addressService;
    }
}
