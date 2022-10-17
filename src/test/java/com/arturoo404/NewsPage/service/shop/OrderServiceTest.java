package com.arturoo404.NewsPage.service.shop;

import com.arturoo404.NewsPage.entity.shop.available.AvailableProduct;
import com.arturoo404.NewsPage.entity.shop.cart.Cart;
import com.arturoo404.NewsPage.entity.shop.cartDetail.CartDetail;
import com.arturoo404.NewsPage.entity.shop.order.Order;
import com.arturoo404.NewsPage.entity.shop.order.dto.OrderDetailDto;
import com.arturoo404.NewsPage.entity.shop.order.dto.OrderDto;
import com.arturoo404.NewsPage.entity.shop.order.dto.OrderUserListDto;
import com.arturoo404.NewsPage.entity.shop.order_detail.OrderDetail;
import com.arturoo404.NewsPage.entity.shop.price.ProductPrice;
import com.arturoo404.NewsPage.entity.shop.product.Product;
import com.arturoo404.NewsPage.entity.user_objects.user.User;
import com.arturoo404.NewsPage.enums.OrderStatus;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.repository.user.UserRepository;
import com.arturoo404.NewsPage.repository.shop.OrderRepository;
import com.arturoo404.NewsPage.repository.shop.ProductRepository;
import com.arturoo404.NewsPage.service.impl.shop.OrderServiceImpl;
import com.arturoo404.NewsPage.validation.PhoneNumberValid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PhoneNumberValid phoneNumberValid;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartService cartService;

    @Mock
    private AvailableProductService availableProductService;


    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderRepository, phoneNumberValid, userRepository, productRepository, cartService, availableProductService);
    }

    @Test
    void itShouldThrowExceptionWhenCartIsEmpty() {
        //Given
        User user = createUser(0);

        //When
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(user));

        try {
            orderService.makeOrder(createOrderDto());
        } catch (ExistInDatabaseException | ParseException e) {
            assertThat(e.getMessage()).isEqualTo("You do not have any product in your cart.");
        }
        //Then
    }

    @Test
    void itShouldThrowExceptionWhenQuantityOfProductIsTooLarge() {
        //Given
        User user = createUser(10);
        Product product = createProduct(5);

        //When
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(user));

        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));

        try {
            orderService.makeOrder(createOrderDto());
        } catch (ExistInDatabaseException | ParseException e) {
            assertThat(e.getMessage()).isEqualTo("We do not have that quantity of this product:\n" +
                    "product");
        }
        //Then
    }

    @Test
    void itShouldReturnSuccessfullyMessage() throws ExistInDatabaseException, ParseException {
        //Given
        User user = createUser(10);
        Product product = createProduct(50);

        //When
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(user));

        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));

        final Object o = orderService.makeOrder(createOrderDto());

        //Then
        assertThat(o.toString()).isEqualTo("Successfully make an order.");
    }

    @Test
    void itShouldThrowExceptionWhenOrderNotFound() {
        //Given
        //When
        when(orderRepository.findByIdAndEmail(anyString(), anyLong()))
                .thenReturn(Optional.empty());

        try {
            orderService.getUserOrderDetail("email@gmail.com", 1L);
        } catch (ExistInDatabaseException e) {
            assertThat(e.getMessage()).isEqualTo("Order not found.");
        }
        //Then
    }

    @Test
    void itShouldReturnOrderDetailDto() throws ExistInDatabaseException {
        //Given
        final Order order = createOrder();
        //When
        when(orderRepository.findByIdAndEmail(anyString(), anyLong()))
                .thenReturn(Optional.of(order));

        final OrderDetailDto userOrderDetail = orderService.getUserOrderDetail("email@gmail.com", 1L);

        //Then
        assertThat(userOrderDetail.getAmount()).isEqualTo(order.getOrderAmount());
        assertThat(userOrderDetail.getOrderStatus()).isEqualTo(order.getOrderStatus());
    }

    @Test
    void itShouldReturnUserOrderList() {
        //Given
        List<Order> orders = List.of(
                createOrder(), createOrder(), createOrder()
        );

        //When
        when(orderRepository.findAllByUserEmail(anyString()))
                .thenReturn(orders);

        final List<OrderUserListDto> userOrderList = orderService.getUserOrderList("email@gmail.com");

        //Then
        assertThat(userOrderList.size()).isEqualTo(userOrderList.size());
    }

    @Test
    void itShouldReturnPageOfUserOrder() {
        //Given
        Page<Order> page = new PageImpl<>(List.of(
                createOrder(), createOrder(), createOrder()
        ));

        //When
        when(orderRepository.findAllByOrderStatus(any(OrderStatus.class), any(Pageable.class)))
                .thenReturn(page);

        final Page<OrderUserListDto> pageOfOrderList = orderService.getPageOfOrderList(1, OrderStatus.NEW);

        //Then
        assertThat(pageOfOrderList.getTotalElements()).isEqualTo(page.getTotalElements());

    }

    private Order createOrder(){
        return Order.builder()
                .user(new User())
                .orderStatus(OrderStatus.NEW)
                .orderDate(Date.from(Instant.now()))
                .commentsToOrder("comment")
                .orderAmount(100D)
                .orderDetails(List.of(
                        OrderDetail.builder()
                                .productQuantity(10L)
                                .productPrice(100D)
                                .product(createProduct(10))
                                .build()
                ))
                .build();
    }

    private User createUser(Integer quantity) {
        return User.builder()
                .cart(Cart.builder()
                        .productQuantity(Long.valueOf(quantity))
                        .cartDetail(List.of(
                                CartDetail.builder()
                                        .quantity( quantity)
                                        .product(Product.builder()
                                                .name("product")
                                                .id(1L)
                                                .build())
                                        .build()
                        ))
                        .build())
                .build();
    }

    private Product createProduct(Integer productQuantity) {
        return Product.builder()
                .id(1L)
                .availableProduct(
                        AvailableProduct.builder()
                                .productQuantity(productQuantity)
                                .build()
                ).productPrice(ProductPrice.builder()
                        .discount(false)
                        .discountPrice(100D)
                        .price(100D)
                        .build())
                .build();
    }

    private OrderDto createOrderDto(){
        return OrderDto.builder()
                .email("test@gmail.com")
                .street("street")
                .phoneNumber(1234567890L)
                .postcode("22222")
                .homeNumber(10)
                .city("city")
                .firstName("first")
                .lastName("last")
                .commentsToOrder("comments")
                .build();
    }
}