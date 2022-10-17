package com.arturoo404.NewsPage.repository.shop;

import com.arturoo404.NewsPage.entity.shop.available.AvailableProduct;
import com.arturoo404.NewsPage.entity.shop.cart.Cart;
import com.arturoo404.NewsPage.entity.shop.cartDetail.CartDetail;
import com.arturoo404.NewsPage.entity.shop.price.ProductPrice;
import com.arturoo404.NewsPage.entity.shop.product.Product;
import com.arturoo404.NewsPage.entity.user_objects.user.User;
import com.arturoo404.NewsPage.enums.ProductCategory;
import com.arturoo404.NewsPage.enums.UserRole;
import com.arturoo404.NewsPage.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class CartDetailRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartDetailRepository cartDetailRepository;

    private String email = "email@gmail.com";

    @Test
    void itShouldFindByIdAndUserEmail() {
        //Given
        final List<CartDetail> cartDetails = createUserWithCart();

        //When
        final Optional<CartDetail> byIdAndUserEmail = cartDetailRepository.findByIdAndUserEmail(email, 1L);

        //Then
        assertThat(byIdAndUserEmail).isPresent();
        assertThat(cartDetails.stream().filter(c -> c.getProduct().getId()
                .equals(byIdAndUserEmail.get().getProduct().getId()))
                .count()).isNotZero();
    }
    
    @Test
    void itShouldUpdateProductQuantity() {
        //Given
        final List<CartDetail> cartDetails = createUserWithCart();

        //When
        cartDetailRepository.updateProductQuantity(20, cartDetails.get(0).getId());
        final Optional<CartDetail> byIdAndUserEmail = cartDetailRepository.findById(cartDetails.get(0).getId());

        //Then
        assertThat(byIdAndUserEmail.get().getQuantity()).isEqualTo(20);
    }

    @Test
    void itShouldDeleteAllCartDetails() {
        //Given
        final List<CartDetail> cartDetails = createUserWithCart();

        //When
        cartDetailRepository.deleteAllCartDetails(cartDetails.get(0).getId());
        final Optional<CartDetail> byIdAndUserEmail = cartDetailRepository.findById(cartDetails.get(0).getId());

        //Then
        assertThat(byIdAndUserEmail).isEmpty();
    }

    private List<CartDetail> createUserWithCart(){
        final Product productTest1 = productRepository.save(product("productTest1"));
        final Product productTest2 = productRepository.save(product("productTest2"));

        final User save = userRepository.save(User.builder()
                .email(email)
                .nick("nick")
                .userRole(UserRole.USER)
                .cart(Cart.builder()
                        .productQuantity(10L)
                        .amount(10D)
                        .build())
                .password(
                        new BCryptPasswordEncoder()
                                .encode("test")
                ).build());

        return cartDetailRepository.saveAll(List.of(
                CartDetail.builder()
                        .cart(save.getCart())
                        .quantity(10)
                        .product(productTest1)
                        .build(),
                CartDetail.builder()
                        .cart(save.getCart())
                        .quantity(20)
                        .product(productTest2)
                        .build()));
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
}