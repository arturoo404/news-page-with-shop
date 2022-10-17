package com.arturoo404.NewsPage.repository.shop;

import com.arturoo404.NewsPage.entity.shop.cart.Cart;
import com.arturoo404.NewsPage.entity.user_objects.user.User;
import com.arturoo404.NewsPage.enums.UserRole;
import com.arturoo404.NewsPage.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class CartRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Test
    void itShouldFindByUserEmail() {
        //Given
        final User userWithCart = createUserWithCart();

        //When
        final Optional<Cart> byUserEmail = cartRepository.findByUserEmail(userWithCart.getEmail());

        //Then
        assertThat(byUserEmail).isPresent();
    }

    @Test
    void itShouldRestartCartStatistic() {
        //Given
        final User userWithCart = createUserWithCart();

        //When
        cartRepository.restartCartStatistic(userWithCart.getId());
        final Optional<Cart> byUserEmail = cartRepository.findByUserEmail(userWithCart.getEmail());

        //Then
        assertThat(byUserEmail.get().getProductQuantity()).isEqualTo(0);
        assertThat(byUserEmail.get().getAmount()).isEqualTo(0);
    }

    private User createUserWithCart(){
        return userRepository.save(User.builder()
                .email("email@gmail.pl")
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
    }
}