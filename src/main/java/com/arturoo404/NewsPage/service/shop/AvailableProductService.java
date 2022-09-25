package com.arturoo404.NewsPage.service.shop;

import com.arturoo404.NewsPage.entity.shop.cartDetail.CartDetail;
import com.arturoo404.NewsPage.entity.shop.product.Product;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;

import java.util.List;


public interface AvailableProductService {

    Product availableStatus(Long productId, Integer quantity) throws ExistInDatabaseException;

    void updateProductQuantity(List<CartDetail> cartDetails) throws ExistInDatabaseException;
}
