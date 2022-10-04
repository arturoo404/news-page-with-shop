package com.arturoo404.NewsPage.repository.shop;

import com.arturoo404.NewsPage.entity.shop.available.AvailableProduct;
import com.arturoo404.NewsPage.entity.shop.price.ProductPrice;
import com.arturoo404.NewsPage.entity.shop.product.Product;
import com.arturoo404.NewsPage.enums.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private String productName = "product";

    @Test
    void itShouldFindProductByName() {
        //Given
        final Product save = productRepository.save(product(productName));

        //When
        final Optional<Product> productByName = productRepository.findProductByName(save.getName());

        //Then
        assertThat(productByName.get().getId()).isEqualTo(save.getId());
    }

    @Test
    void itShouldFindPageOfALllAvailableProduct() {
        //Given
        final List<Product> products = productRepository.saveAll(
                List.of(
                        product(productName),
                        product(productName + "1"),
                        product(productName + "2"),
                        productStatusFalse(productName + "False")
                ));
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").descending());

        //When
        final Page<Product> aLllAvailableProduct = productRepository.findALllAvailableProduct(pageable);

        //Then
        assertThat(aLllAvailableProduct.stream()
                .filter(a -> a.getAvailableProduct().isAvailableStatus())
                .count()).isNotEqualTo(products.size());
    }

    @Test
    void itShouldFindByIdAndStatus() {
        //Given
        final Product save = productRepository.save(product(productName));

        //When
        final Optional<Product> byIdAndStatus = productRepository.findByIdAndStatus(save.getId());

        //Then
        assertThat(byIdAndStatus.get().getName()).isEqualTo(save.getName());
    }

    @Test
    void itShouldNotFindByIdAndStatus() {
        //Given
        final Product save = productRepository.save(productStatusFalse(productName));

        //When
        final Optional<Product> byIdAndStatus = productRepository.findByIdAndStatus(save.getId());

        //Then
        assertThat(byIdAndStatus).isEmpty();
    }

    @Test
    void findALllAvailableProductByCategory() {
        //Given
        final List<Product> products = productRepository.saveAll(
                List.of(
                        product(productName),
                        product(productName + "1"),
                        product(productName + "2"),
                        productStatusFalse(productName + "False")
                ));
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").descending());

        //When
        final Page<Product> aLllAvailableProductByCategory =
                productRepository.findALllAvailableProductByCategory(pageable, ProductCategory.OTHER);

        //Then
        assertThat(aLllAvailableProductByCategory.stream()
                .filter(a -> a.getAvailableProduct().isAvailableStatus())
                .count()).isNotEqualTo(products.size());
    }

    private Product product(String productName){
        return Product.builder()
                .productCategory(ProductCategory.OTHER)
                .name(productName)
                .productPrice(ProductPrice.builder()
                        .price(100D)
                        .discountPrice(100D)
                        .discount(false)
                        .build())
                .availableProduct(AvailableProduct.builder()
                        .productQuantity(10)
                        .availableStatus(true)
                        .build())
                .build();
    }

    private Product productStatusFalse(String productName){
        return Product.builder()
                .productCategory(ProductCategory.OTHER)
                .name(productName)
                .productPrice(ProductPrice.builder()
                        .price(100D)
                        .discountPrice(100D)
                        .discount(false)
                        .build())
                .availableProduct(AvailableProduct.builder()
                        .productQuantity(10)
                        .availableStatus(false)
                        .build())
                .build();
    }
}