package com.arturoo404.NewsPage.service.shop;

import com.arturoo404.NewsPage.entity.shop.product.Product;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;


public interface AvailableProductService {

    Product availableStatus(Long productId, Integer quantity) throws ExistInDatabaseException;
}
