package com.arturoo404.NewsPage.controller.api.shop;

import com.arturoo404.NewsPage.entity.shop.product.dto.ProductCreateDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.service.shop.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping( "/api/shop/product")
public class ProductApiController {

    private final ProductService productService;

    @Autowired
    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(path = "/create")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOY')")
    public ResponseEntity<Object> createProduct(@RequestBody ProductCreateDto p){
        if (p.getName().isBlank() || p.getPrice() < 0 || p.getDescription().isBlank()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("One or more filed is empty.");
        }

        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(productService.createProduct(p));
        } catch (ExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PatchMapping(path = "/photo/set/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOY')")
    public ResponseEntity<?> productPhoto(@RequestParam("file") MultipartFile photo,
                                             @PathVariable("id") Long id) throws IOException {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(productService.setProductPhoto(photo, id));
        } catch (ExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @GetMapping(path = "/list")
    public ResponseEntity<Page<?>> productPage(@RequestParam("page") Integer page){
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProductList(page));
    }
}
