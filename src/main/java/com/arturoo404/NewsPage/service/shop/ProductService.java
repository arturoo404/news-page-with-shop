package com.arturoo404.NewsPage.service.shop;

import com.arturoo404.NewsPage.entity.shop.product.dto.ProductCreateDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    Object createProduct(ProductCreateDto productCreateDto) throws ExistInDatabaseException;

    Object setProductPhoto(MultipartFile photo, Long id) throws ExistInDatabaseException, IOException;
}
