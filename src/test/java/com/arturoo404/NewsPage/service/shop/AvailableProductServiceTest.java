package com.arturoo404.NewsPage.service.shop;

import com.arturoo404.NewsPage.entity.shop.available.AvailableProduct;
import com.arturoo404.NewsPage.entity.shop.price.ProductPrice;
import com.arturoo404.NewsPage.entity.shop.product.Product;
import com.arturoo404.NewsPage.enums.ProductCategory;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.repository.shop.ProductRepository;
import com.arturoo404.NewsPage.service.impl.shop.AvailableProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AvailableProductServiceTest {

    private AvailableProductService availableProductService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        availableProductService = new AvailableProductServiceImpl(productRepository);
    }

    @Test
    void itShouldThrowExistInDataBaseExceptionWhenProductNotFound() {
        //Given
        //When
        when(productRepository.findByIdAndStatus(anyLong()))
                .thenReturn(Optional.empty());

        try {
            availableProductService.availableStatus(1L, 5);
        } catch (ExistInDatabaseException e) {
            assertThat(e.getMessage()).isEqualTo("We did not find this product.");
        }
    }

    @Test
    void itShouldThrowExistInDataBaseExceptionWhenProductQuantityIsLessThatRequest() {
        //Given
        Product product = product();

        //When
        when(productRepository.findByIdAndStatus(anyLong()))
                .thenReturn(Optional.of(product));

        try {
            availableProductService.availableStatus(1L, 100);
        } catch (ExistInDatabaseException e) {
            assertThat(e.getMessage()).isEqualTo("We do not have that many product, please select less number of it.");
        }
    }

    @Test
    void itShouldReturnProductEntity() throws ExistInDatabaseException {
        //Given
        Product product = product();

        //When
        when(productRepository.findByIdAndStatus(anyLong()))
                .thenReturn(Optional.of(product));

        final Product productFromDb = availableProductService.availableStatus(1L, 10);

        //Then
        assertThat(productFromDb).isNotNull();
    }

    private Product product(){
        return Product.builder()
                .id(1L)
                .productCategory(ProductCategory.OTHER)
                .name("name")
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