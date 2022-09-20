package com.arturoo404.NewsPage.repository.shop;

import com.arturoo404.NewsPage.entity.shop.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
