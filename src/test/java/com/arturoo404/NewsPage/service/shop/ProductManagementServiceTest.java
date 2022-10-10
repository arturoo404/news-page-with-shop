package com.arturoo404.NewsPage.service.shop;

import com.arturoo404.NewsPage.entity.shop.available.AvailableProduct;
import com.arturoo404.NewsPage.entity.shop.price.ProductPrice;
import com.arturoo404.NewsPage.entity.shop.product.Product;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.repository.shop.AvailableProductRepository;
import com.arturoo404.NewsPage.repository.shop.ProductPriceRepository;
import com.arturoo404.NewsPage.service.impl.shop.ProductManagementServiceImpl;
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
class ProductManagementServiceTest {


    private ProductManagementService productManagementService;

    @Mock
    private ProductPriceRepository productPriceRepository;

    @Mock
    private AvailableProductRepository availableProductRepository;

    @BeforeEach
    void setUp() {
        productManagementService = new ProductManagementServiceImpl(availableProductRepository, productPriceRepository);
    }

    @Test
    void itShouldThrowExceptionWhenProductNotExist() {
        //Given
        //When
        when(availableProductRepository.findAvailableProductById(anyLong()))
                .thenReturn(Optional.empty());

        try {
            productManagementService.updateProductStatus(1L, false);
        } catch (ExistInDatabaseException e) {
            assertThat(e.getMessage()).isEqualTo("This product is not exist in database.");
        }
        //Then
    }
}