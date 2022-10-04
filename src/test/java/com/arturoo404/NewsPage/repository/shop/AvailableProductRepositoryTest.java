package com.arturoo404.NewsPage.repository.shop;

import com.arturoo404.NewsPage.entity.shop.available.AvailableProduct;
import com.arturoo404.NewsPage.entity.shop.price.ProductPrice;
import com.arturoo404.NewsPage.entity.shop.product.Product;
import com.arturoo404.NewsPage.enums.ProductCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AvailableProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AvailableProductRepository availableProductRepository;

    @Test
    void findAvailableProductById() {
        //Given
        final Product save = productRepository.save(product());

        //When
        final Optional<AvailableProduct> availableProductById = availableProductRepository.findAvailableProductById(save.getId());

        //Then
        assertThat(availableProductById.get().isAvailableStatus()).isTrue();
        assertThat(save.getAvailableProduct().getProductQuantity()).isEqualTo(availableProductById.get().getProductQuantity());
    }

    private Product product(){
        return Product.builder()
                .productCategory(ProductCategory.OTHER)
                .name("product")
                .availableProduct(AvailableProduct.builder()
                        .productQuantity(10)
                        .availableStatus(true)
                        .build())
                .build();
    }
}