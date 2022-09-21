package com.arturoo404.NewsPage.repository.shop;

import com.arturoo404.NewsPage.entity.shop.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
