package com.arturoo404.NewsPage.repository.shop;

import com.arturoo404.NewsPage.entity.shop.available.AvailableProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvailableProductRepository extends JpaRepository<AvailableProduct, Long> {

    @Query(value = "FROM AvailableProduct a WHERE a.product.id = ?1")
    Optional<AvailableProduct> findAvailableProductById(Long id);
}
