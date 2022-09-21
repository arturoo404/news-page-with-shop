package com.arturoo404.NewsPage.service.impl.shop;

import com.arturoo404.NewsPage.repository.shop.CartRepository;
import com.arturoo404.NewsPage.service.shop.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }
}
