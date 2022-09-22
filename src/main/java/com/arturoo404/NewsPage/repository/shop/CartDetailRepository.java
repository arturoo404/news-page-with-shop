package com.arturoo404.NewsPage.repository.shop;

import com.arturoo404.NewsPage.entity.shop.cartDetail.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE CartDetail c SET c.quantity = ?1 WHERE c.id = ?2")
    void updateProductQuantity(Integer quantity, Long id);
}
