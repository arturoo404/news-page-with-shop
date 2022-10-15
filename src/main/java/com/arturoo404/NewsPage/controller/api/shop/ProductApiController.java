package com.arturoo404.NewsPage.controller.api.shop;

import com.arturoo404.NewsPage.entity.news.photo.dto.PhotoDto;
import com.arturoo404.NewsPage.entity.shop.product.dto.ProductCreateDto;
import com.arturoo404.NewsPage.entity.shop.product.dto.ProductPageDto;
import com.arturoo404.NewsPage.enums.ProductCategory;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.service.shop.ProductService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PostMapping(path = "/photo/set/{id}")
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
    public ResponseEntity<Page<ProductPageDto>> productPage(@RequestParam("page") Integer page){
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProductList(page));
    }

    @GetMapping(path = "/list/{category}")
    public ResponseEntity<Page<ProductPageDto>> productPageByCategory(@RequestParam("page") Integer page,
                                                         @PathVariable("category") ProductCategory productCategory){
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProductListByCategory(page, productCategory));
    }

    @GetMapping(path = "/photo/get/{id}")
    public void productPhoto(@PathVariable Long id, HttpServletResponse response) throws IOException {
        PhotoDto photoDto = productService.getProductPhoto(id);

        response.setContentType("image/jpeg");
        InputStream is = new ByteArrayInputStream(photoDto.getPhoto());
        IOUtils.copy(is, response.getOutputStream());
    }

    @GetMapping(path = "/detail")
    public ResponseEntity<Object> productDetail(@RequestParam("id") Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(productService.productDetail(id));
        } catch (ExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
