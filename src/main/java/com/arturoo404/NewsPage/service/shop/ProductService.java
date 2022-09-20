package com.arturoo404.NewsPage.service.shop;

import com.arturoo404.NewsPage.entity.shop.product.dto.ProductCreateDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;

public interface ProductService {
    Object createProduct(ProductCreateDto productCreateDto) throws ExistInDatabaseException;
}
