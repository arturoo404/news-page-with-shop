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
class ProductPriceRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductPriceRepository productPriceRepository;

    @Test
    void itShouldFindProductPriceByProductId() {
        //Given
        final Product save = productRepository.save(product());

        //When
        final Optional<ProductPrice> productPriceByProductId = productPriceRepository.findProductPriceByProductId(save.getId());

        //Then
        assertThat(productPriceByProductId).isPresent();
        assertThat(productPriceByProductId.get().getPrice()).isEqualTo(save.getProductPrice().getPrice());
        assertThat(productPriceByProductId.get().isDiscount()).isFalse();
    }


    private Product product(){
        return Product.builder()
                .productCategory(ProductCategory.OTHER)
                .name("product")
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
}