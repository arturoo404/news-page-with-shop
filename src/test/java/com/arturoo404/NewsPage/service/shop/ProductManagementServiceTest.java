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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductManagementServiceTest {


    private ProductManagementService productManagementService;

    @Mock
    private ProductPriceRepository productPriceRepository;

    @Mock
    private AvailableProductRepository availableProductRepository;

    @Captor
    ArgumentCaptor<AvailableProduct> availableProductArgumentCaptor;

    @Captor
    ArgumentCaptor<ProductPrice> productPriceArgumentCaptor;

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

    @Test
    void itShouldUpdateStatusAndReturnUpdatedObject() throws ExistInDatabaseException {
        //Given
        boolean productStatus = false;

        //When
        when(availableProductRepository.findAvailableProductById(anyLong()))
                .thenReturn(Optional.of(createAvailableProduct()));

        when(availableProductRepository.save(availableProductArgumentCaptor.capture()))
                .thenReturn(new AvailableProduct());

        productManagementService.updateProductStatus(1L, productStatus);

        //Then
        assertThat(availableProductArgumentCaptor.getValue().isAvailableStatus()).isEqualTo(productStatus);
    }

    @Test
    void itShouldUpdateProductQuantityAndReturnObject() throws ExistInDatabaseException {
        //Given
        Integer quantity = 100;

        //When
        when(availableProductRepository.findAvailableProductById(anyLong()))
                .thenReturn(Optional.of(createAvailableProduct()));

        when(availableProductRepository.save(availableProductArgumentCaptor.capture()))
                .thenReturn(new AvailableProduct());

        productManagementService.updateProductQuantity(1L, quantity);

        //Then
        assertThat(availableProductArgumentCaptor.getValue().getProductQuantity()).isEqualTo(100);
    }

    @Test
    void itShouldUpdateProductPriceAndReturnObject() throws ExistInDatabaseException {
        //Given
        Double price = 150D;

        //When
        when(productPriceRepository.findProductPriceByProductId(anyLong()))
                .thenReturn(Optional.of(createProductPrice()));

        when(productPriceRepository.save(productPriceArgumentCaptor.capture()))
                .thenReturn(new ProductPrice());

        productManagementService.updateProductPrice(1L, price);

        //Then
        assertThat(productPriceArgumentCaptor.getValue().getPrice()).isEqualTo(price);
    }

    @Test
    void itShouldUpdateProductDiscountPriceAndReturnObject() throws ExistInDatabaseException {
        //Given
        Double discountPrice = 120D;

        //When
        when(productPriceRepository.findProductPriceByProductId(anyLong()))
                .thenReturn(Optional.of(createProductPrice()));

        when(productPriceRepository.save(productPriceArgumentCaptor.capture()))
                .thenReturn(new ProductPrice());

        productManagementService.updateProductDiscountPrice(1L, discountPrice);

        //Then
        assertThat(productPriceArgumentCaptor.getValue().getDiscountPrice()).isEqualTo(discountPrice);
    }

    @Test
    void itShouldUpdatePromotionStatusAndReturnObject() throws ExistInDatabaseException {
        //Given
        boolean status = true;

        //When
        when(productPriceRepository.findProductPriceByProductId(anyLong()))
                .thenReturn(Optional.of(createProductPrice()));

        when(productPriceRepository.save(productPriceArgumentCaptor.capture()))
                .thenReturn(new ProductPrice());

        productManagementService.updatePromotionStatus(1L, status);

        //Then
        assertThat(productPriceArgumentCaptor.getValue().isDiscount()).isEqualTo(status);
    }


    private AvailableProduct createAvailableProduct(){
        return AvailableProduct.builder()
                .product(Product.builder()
                        .id(1L)
                        .productPrice(ProductPrice.builder()
                                .discount(false)
                                .discountPrice(100D)
                                .price(100D)
                                .build())
                        .build())
                .availableStatus(true)
                .productQuantity(10)
                .build();
    }

    private ProductPrice createProductPrice(){
        return ProductPrice.builder()
                .product(Product.builder()
                        .id(1L)
                        .availableProduct(AvailableProduct.builder()
                                .availableStatus(true)
                                .productQuantity(10)
                                .build())
                        .build())
                .discount(false)
                .discountPrice(100D)
                .price(100D)
                .build();
    }
}