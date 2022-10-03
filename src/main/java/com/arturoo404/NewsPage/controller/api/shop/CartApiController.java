package com.arturoo404.NewsPage.controller.api.shop;

import com.arturoo404.NewsPage.entity.shop.cart.dto.CartNavInfoDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.service.shop.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/api/shop/cart")
public class CartApiController {

    private final CartService cartService;

    @Autowired
    public CartApiController(CartService cartService) {
        this.cartService = cartService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "/add-product")
    public ResponseEntity<?> cart(@RequestParam("email") String email,
                                  @RequestParam("productId") Long id,
                                  @RequestParam(value = "quantity", defaultValue = "1") Integer quantity){
        if (quantity < 1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Select number of product.");
        }

        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(cartService.addProductToCart(email, id, quantity));
        } catch (ExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/nav-info")
    public ResponseEntity<Object> cartNavbarInfo(@RequestParam("email") String email){
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(cartService.findCartNavInfo(email));
        } catch (ExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/detail")
    public ResponseEntity<Object> cartDetail(@RequestParam("email") String email){
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(cartService.findCartDetail(email));
        } catch (ExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-product")
    public ResponseEntity<Object> deleteProductFromCart(@RequestParam("email") String email,
                                                        @RequestParam("productId") Long id,
                                                        @RequestParam("quantity") Integer quantity){
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(cartService.deleteProductFromCart(email, id, quantity));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
