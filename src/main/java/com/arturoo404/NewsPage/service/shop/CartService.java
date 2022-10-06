package com.arturoo404.NewsPage.service.shop;

import com.arturoo404.NewsPage.entity.shop.cart.Cart;
import com.arturoo404.NewsPage.entity.shop.cart.dto.CartDetailDto;
import com.arturoo404.NewsPage.entity.shop.cart.dto.CartNavInfoDto;
import com.arturoo404.NewsPage.entity.shop.cartDetail.CartDetail;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;

import java.util.List;

public interface CartService {
    Object addProductToCart(String email, Long id, Integer quantity) throws ExistInDatabaseException;

    CartNavInfoDto findCartNavInfo(String email) throws ExistInDatabaseException;

    CartDetailDto findCartDetail(String email) throws ExistInDatabaseException;

    Object deleteProductFromCart(String email, Long id, Integer quantity) throws ExistInDatabaseException;

    void deleteCartDetail(Long cartId);

    void restartStatistic(Long id);
}
