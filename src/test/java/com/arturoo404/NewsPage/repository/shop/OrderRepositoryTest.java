package com.arturoo404.NewsPage.repository.shop;

import com.arturoo404.NewsPage.entity.shop.order.Order;
import com.arturoo404.NewsPage.entity.shop.order_adderss.OrderAddress;
import com.arturoo404.NewsPage.entity.user_objects.user.User;
import com.arturoo404.NewsPage.enums.OrderStatus;
import com.arturoo404.NewsPage.enums.UserRole;
import com.arturoo404.NewsPage.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void itShouldFindAllByUserEmail() {
        //Given
        final List<Order> orders = createOrders();

        //When
        final List<Order> allByUserEmail = orderRepository.findAllByUserEmail(orders.get(0).getUser().getEmail());

        //Then
        assertThat(orders.size()).isEqualTo(allByUserEmail.size());

    }

    @Test
    void itShouldFindByIdAndEmail() {
        //Given
        final List<Order> orders = createOrders();

        //When
        final Optional<Order> byIdAndEmail = orderRepository.findByIdAndEmail(orders.get(0).getUser().getEmail(), orders.get(0).getId());

        //Then
        assertThat(byIdAndEmail).isPresent();
        assertThat(byIdAndEmail.get()).isEqualTo(orders.get(0));
    }

    @Test
    void itShouldFindAllByOrderStatus() {
        //Given
        final List<Order> orders = createOrders();
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").descending());

        //When
        final Page<Order> allByOrderStatus = orderRepository.findAllByOrderStatus(OrderStatus.NEW, pageable);

        //Then
        assertThat(orders.stream()
                .filter(o -> o.getOrderStatus().equals(OrderStatus.NEW))
                .count()).isEqualTo(allByOrderStatus.getTotalElements());
    }

    private List<Order> createOrders(){
        final User user = createUser();
        return orderRepository.saveAll(List.of(
                Order.builder()
                        .user(user)
                        .orderStatus(OrderStatus.NEW)
                        .orderDate(Date.from(Instant.now()))
                        .commentsToOrder("comment")
                        .ordersAddress(getBuild())
                        .orderAmount(100D)
                        .build(),
                Order.builder()
                        .user(user)
                        .orderStatus(OrderStatus.REALIZED)
                        .orderDate(Date.from(Instant.now()))
                        .commentsToOrder("comment")
                        .ordersAddress(getBuild())
                        .orderAmount(100D)
                        .build()
        ));
    }

    private OrderAddress getBuild() {
        return OrderAddress.builder()
                .street("street")
                .phoneNumber("12345677")
                .postcode("22222")
                .homeNumber("10")
                .city("city")
                .firstName("first")
                .lastName("last")
                .build();
    }


    private User createUser(){
        return userRepository.save(User.builder()
                .email("email@gmail.com")
                .nick("nick")
                .userRole(UserRole.USER)
                .password(
                        new BCryptPasswordEncoder()
                                .encode("test")
                ).build());
    }
}