package com.arturoo404.NewsPage.repository.shop;

import com.arturoo404.NewsPage.entity.shop.price.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {

    @Query(value = "FROM ProductPrice p WHERE p.product.id = ?1")
    Optional<ProductPrice> findProductPriceByProductId(Long id);
}
