package com.arturoo404.NewsPage.service.shop;

import com.arturoo404.NewsPage.entity.news.photo.dto.PhotoDto;
import com.arturoo404.NewsPage.entity.shop.available.AvailableProduct;
import com.arturoo404.NewsPage.entity.shop.price.ProductPrice;
import com.arturoo404.NewsPage.entity.shop.product.Product;
import com.arturoo404.NewsPage.entity.shop.product.dto.ProductCreateDto;
import com.arturoo404.NewsPage.entity.shop.product.dto.ProductDetail;
import com.arturoo404.NewsPage.entity.shop.product.dto.ProductPageDto;
import com.arturoo404.NewsPage.enums.ProductCategory;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.repository.shop.ProductRepository;
import com.arturoo404.NewsPage.service.impl.shop.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Captor
    ArgumentCaptor<Product> productArgumentCaptor;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    void itShouldThrowExceptionWhenProductExist() {
        //Given
        //When
        when(productRepository.findProductByName(anyString()))
                .thenReturn(Optional.of(createProduct()));

        try {
            productService.createProduct(creatProductDto());
        } catch (ExistInDatabaseException e) {
            assertThat(e.getMessage()).isEqualTo("This product exist in database, change name and try again.");
        }
        //Then
    }

    @Test
    void itShouldSaveAndReturnCreatedObject() throws ExistInDatabaseException {
        //Given
        ProductCreateDto productCreateDto = creatProductDto();

        //When
        when(productRepository.findProductByName(anyString()))
                .thenReturn(Optional.empty());

        when(productRepository.save(productArgumentCaptor.capture()))
                .thenReturn(new Product());

        productService.createProduct(productCreateDto);

        //Then
        assertThat(productArgumentCaptor.getValue().getName()).isEqualTo(productCreateDto.getName());
        assertThat(productArgumentCaptor.getValue().getAvailableProduct().getProductQuantity())
                .isEqualTo(productCreateDto.getProductQuantity());
        assertThat(productArgumentCaptor.getValue().getProductCategory()).isEqualTo(productCreateDto.getProductCategory());
    }

    @Test
    void itShouldThrowExceptionWhenProductNotExist() {
        //Given
        MockMultipartFile photo =
                new MockMultipartFile("file", "filename.png", "image/jpeg", "10 10 10".getBytes());

        //When
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        try {
            productService.setProductPhoto(photo, 1L);
        } catch (ExistInDatabaseException | IOException e) {
            assertThat(e.getMessage()).isEqualTo("This product is not exist in database.");
        }
        //Then
    }

    @Test
    void itShouldSaveProductAndReturnObject() throws ExistInDatabaseException, IOException {
        //Given
        MockMultipartFile photo =
                new MockMultipartFile("file", "filename.png", "image/jpeg", "10 10 10".getBytes());

        //When
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(createProduct()));

        when(productRepository.save(productArgumentCaptor.capture()))
                .thenReturn(new Product());

        productService.setProductPhoto(photo, 1L);
        //Then
        assertThat(productArgumentCaptor.getValue().getPhoto().length)
                .isEqualTo(photo.getBytes().length);
    }

    @Test
    void itShouldReturnPageOfAllProduct() {
        //Given
        Page<Product> page = new PageImpl<>(List.of(
                createProduct(), createProduct(), createProduct()
        ));

        //When
        when(productRepository.findALllAvailableProduct(any(Pageable.class)))
                .thenReturn(page);

        final Page<ProductPageDto> productList = productService.getProductList(1);

        //Then
        assertThat(productList.getTotalElements()).isEqualTo(page.getTotalElements());
    }

    @Test
    void itShouldReturnProductPhoto() {
        //Given
        Product product = createProduct();
        product.setPhoto(new byte[]{10, 10, 10});

        //When
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));

        final PhotoDto productPhoto = productService.getProductPhoto(1L);
        //Then
        assertThat(productPhoto.getPhoto().length).isEqualTo(product.getPhoto().length);
    }

    @Test
    void productDetail() throws ExistInDatabaseException {
        //Given
        Product product = createProduct();

        //When
        when(productRepository.findByIdAndStatus(anyLong()))
                .thenReturn(Optional.of(product));

        final ProductDetail productDetail = productService.productDetail(1L);
        //Then
        assertThat(productDetail.getName()).isEqualTo(product.getName());
        assertThat(productDetail.getProductQuantity()).isEqualTo(product.getAvailableProduct().getProductQuantity());
        assertThat(productDetail.getDescription()).isEqualTo(product.getDescription());
    }

    @Test
    void getProductListByCategory() {
        //Given
        Page<Product> page = new PageImpl<>(List.of(
                createProduct(), createProduct(), createProduct()
        ));

        //When
        when(productRepository.findALllAvailableProductByCategory(any(Pageable.class), any(ProductCategory.class)))
                .thenReturn(page);

        final Page<ProductPageDto> productListByCategory = productService.getProductListByCategory(1, ProductCategory.OTHER);

        //Then
        assertThat(productListByCategory.getTotalElements()).isEqualTo(page.getTotalElements());
    }

    private Product createProduct() {
        return Product.builder()
                .id(1L)
                .availableProduct(
                        AvailableProduct.builder()
                                .productQuantity(100)
                                .build()
                ).productPrice(ProductPrice.builder()
                        .discount(false)
                        .discountPrice(100D)
                        .price(100D)
                        .build())
                .build();
    }

    private ProductCreateDto creatProductDto(){
        return ProductCreateDto.builder()
                .name("product")
                .description("description")
                .price(100D)
                .productCategory(ProductCategory.OTHER)
                .productQuantity(100)
                .build();
    }
}