package com.arturoo404.NewsPage.controller.api.shop;

import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.service.shop.ProductManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/shop/product-management")
public class ProductManagementApiController {


    private final ProductManagementService productManagementService;

    @Autowired
    public ProductManagementApiController(ProductManagementService productManagementService) {
        this.productManagementService = productManagementService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOY')")
    @PatchMapping(path = "/update-status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable("id") Long id,
                                          @RequestParam("status") Boolean status){
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(productManagementService.updateProductStatus(id, status));
        } catch (ExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOY')")
    @PatchMapping(path = "/product-quantity/{id}")
    public ResponseEntity<?> updateQuantityOfProduct(@PathVariable("id") Long id,
                                                     @RequestParam("quantity") Integer quantity){
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(productManagementService.updateProductQuantity(id, quantity));
        } catch (ExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PatchMapping(path = "/change-price/{id}")
    public ResponseEntity<?> changeProductPrice(@PathVariable("id") Long id,
                                                @RequestParam("price") Double price){
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(productManagementService.updateProductPrice(id, price));
        } catch (ExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
