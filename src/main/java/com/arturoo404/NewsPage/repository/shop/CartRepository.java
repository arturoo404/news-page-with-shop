package com.arturoo404.NewsPage.repository.shop;

import com.arturoo404.NewsPage.entity.shop.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserEmail(String email);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Cart c SET c.amount = 0, c.productQuantity = 0 WHERE c.id = ?1")
    void restartCartStatistic(Long id);
}
