package com.arturoo404.NewsPage.service.shop;

import com.arturoo404.NewsPage.entity.shop.available.AvailableProduct;
import com.arturoo404.NewsPage.entity.shop.cart.Cart;
import com.arturoo404.NewsPage.entity.shop.cart.dto.CartDetailDto;
import com.arturoo404.NewsPage.entity.shop.cart.dto.CartNavInfoDto;
import com.arturoo404.NewsPage.entity.shop.cartDetail.CartDetail;
import com.arturoo404.NewsPage.entity.shop.price.ProductPrice;
import com.arturoo404.NewsPage.entity.shop.product.Product;
import com.arturoo404.NewsPage.enums.ProductCategory;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.repository.shop.CartDetailRepository;
import com.arturoo404.NewsPage.repository.shop.CartRepository;
import com.arturoo404.NewsPage.repository.shop.ProductRepository;
import com.arturoo404.NewsPage.service.impl.shop.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartDetailRepository cartDetailRepository;

    @Mock
    private AvailableProductService availableProductService;

    private String email = "email@gmail.com";
    private Long id = 1L;
    private Integer quantity = 10;

    @BeforeEach
    void setUp() {
        cartService = new CartServiceImpl(cartRepository, productRepository, cartDetailRepository, availableProductService);
    }

    @Test
    void itShouldThrowExceptionWhenUserNotExist() {
        //Given
        //When
        when(cartRepository.findByUserEmail(anyString()))
                .thenReturn(Optional.empty());

        try {
            cartService.addProductToCart(email, id, quantity);
        } catch (ExistInDatabaseException e) {
            assertThat(e.getMessage()).isEqualTo("We did not find user with this email: " + email);
        }
        //Then
    }

    @Test
    void itShouldAddProductToCart() throws ExistInDatabaseException {
        //Given
        Cart cart = createCart();

        //When
        when(cartRepository.findByUserEmail(anyString()))
                .thenReturn(Optional.of(cart));

        when(availableProductService.availableStatus(anyLong(), anyInt()))
                .thenReturn(createProduct());

        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(createProduct()));

        final Object o = cartService.addProductToCart(email, id, quantity);
        //Then
        assertThat(o.toString())
                .isEqualTo("Product has added");
    }

    @Test
    void itShouldThrowExceptionWhenUserNotExistInDatabase() {
        //Given
        //When
        when(cartRepository.findByUserEmail(anyString()))
                .thenReturn(Optional.empty());

        try {
            cartService.findCartNavInfo(email);
        } catch (ExistInDatabaseException e) {
            assertThat(e.getMessage()).isEqualTo("User does not exist.");
        }
        //Then
    }

    @Test
    void itShouldReturnNavInfo() throws ExistInDatabaseException {
        //Given
        Cart cart = createCart();

        //When
        when(cartRepository.findByUserEmail(anyString()))
                .thenReturn(Optional.of(createCart()));

        final CartNavInfoDto cartNavInfo = cartService.findCartNavInfo(email);

        //Then
        assertThat(cartNavInfo.getAmount()).isEqualTo(cart.getAmount());
        assertThat(cartNavInfo.getQuantity()).isEqualTo(cart.getProductQuantity());
    }

    @Test
    void findCartDetail() throws ExistInDatabaseException {
        //Given
        Cart cart = createCart();

        //When
        when(cartRepository.findByUserEmail(anyString()))
                .thenReturn(Optional.of(createCart()));

        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(createProduct()));

        final CartDetailDto cartDetail = cartService.findCartDetail(email);

        //Then
        assertThat(cartDetail.getAmount()).isEqualTo(cart.getAmount());
        assertThat(cartDetail.getQuantity()).isEqualTo(cart.getProductQuantity());
    }

    @Test
    void itShouldThrowExceptionWhenDeleteEntityDoesNotExist() {
        //Given
        //When
        when(cartDetailRepository.findByIdAndUserEmail(anyString(), anyLong()))
                .thenReturn(Optional.empty());

        try {
            cartService.deleteProductFromCart(email, id, quantity);
        } catch (ExistInDatabaseException e) {
            assertThat(e.getMessage()).isEqualTo("Entity does not exist.");
        }
        //Then
    }

    @Test
    void itShouldThrowExceptionWhenQuantityOfDeleteEntityIsTooLarge() {
        //Given
        //When
        when(cartDetailRepository.findByIdAndUserEmail(anyString(), anyLong()))
                .thenReturn(Optional.of(createCartDetail()));

        try {
            cartService.deleteProductFromCart(email, id, 1000);
        } catch (Exception e) {
             assertThat(e.getMessage()).isEqualTo("Item quantity is too large.");
        }
        //Then
    }

    @Test
    void itShouldReturnSuccessfullyDeleteMessage() throws ExistInDatabaseException {
        //Given
        //When
        when(cartDetailRepository.findByIdAndUserEmail(anyString(), anyLong()))
                .thenReturn(Optional.of(createCartDetail()));

        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(createProduct()));

        final Object o = cartService.deleteProductFromCart(email, id, quantity);
        //Then
        assertThat(o.toString()).isEqualTo("Successfully deleted product from cart.");
    }

    private CartDetail createCartDetail(){
        return CartDetail.builder()
                .id(1L)
                .quantity(10)
                .product(createProduct())
                .cart(createCart())
                .build();
    }
    private Cart createCart(){
        return Cart.builder()
                .id(1L)
                .cartDetail(List.of(
                        CartDetail.builder()
                                .id(10L)
                                .cart(new Cart())
                                .product(createProduct())
                                .quantity(10)
                                .build()
                ))
                .productQuantity(10L)
                .amount(10D)
                .build();
    }

    private Product createProduct(){
        return Product.builder()
                .id(1L)
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