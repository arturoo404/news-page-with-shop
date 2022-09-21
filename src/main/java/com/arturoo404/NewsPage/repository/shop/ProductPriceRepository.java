package com.arturoo404.NewsPage.repository.shop;

import com.arturoo404.NewsPage.entity.shop.price.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
}
