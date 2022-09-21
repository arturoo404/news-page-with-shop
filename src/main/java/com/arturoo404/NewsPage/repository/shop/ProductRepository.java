package com.arturoo404.NewsPage.repository.shop;

import com.arturoo404.NewsPage.entity.shop.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductByName(String name);

    @Query(value = "FROM Product p WHERE p.availableProduct.availableStatus = true")
    Page<Product> findALllAvailableProduct(Pageable pageable);

    @Query(value ="FROM Product p WHERE p.availableProduct.availableStatus = true AND p.id = ?1")
    Optional<Product> findByIdAndStatus(Long id);
}
