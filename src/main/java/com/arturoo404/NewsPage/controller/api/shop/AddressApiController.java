package com.arturoo404.NewsPage.controller.api.shop;

import com.arturoo404.NewsPage.entity.shop.address.dto.AddressDto;
import com.arturoo404.NewsPage.exception.ValidException;
import com.arturoo404.NewsPage.service.shop.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(path = "/api/shop/address")
public class AddressApiController {


    private final AddressService addressService;

    @Autowired
    public AddressApiController(AddressService addressService) {
        this.addressService = addressService;
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PATCH}, path = "/update")
    public ResponseEntity<?> updateUserAddress(@RequestBody AddressDto addressDto){
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(addressService.updateAddress(addressDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
