package com.arturoo404.NewsPage.service.shop;

import com.arturoo404.NewsPage.entity.shop.cart.Cart;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;

public interface CartService {
    Cart addProductToCart(String email, Long id, Integer quantity) throws ExistInDatabaseException;

    Object findCartNavInfo(String email) throws ExistInDatabaseException;
}
