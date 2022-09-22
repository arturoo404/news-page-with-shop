package com.arturoo404.NewsPage.service.shop;

import com.arturoo404.NewsPage.entity.shop.cart.Cart;
import com.arturoo404.NewsPage.entity.shop.cart.dto.CartNavInfoDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;

public interface CartService {
    Cart addProductToCart(String email, Long id, Integer quantity) throws ExistInDatabaseException;

    CartNavInfoDto findCartNavInfo(String email) throws ExistInDatabaseException;

    Object findCartDetail(String email) throws ExistInDatabaseException;

    Object deleteProductFromCart(String email, Long id, Integer quantity) throws ExistInDatabaseException;
}
