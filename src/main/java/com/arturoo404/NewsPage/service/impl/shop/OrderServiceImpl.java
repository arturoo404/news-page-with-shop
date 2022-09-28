package com.arturoo404.NewsPage.service.impl.shop;

import com.arturoo404.NewsPage.entity.shop.cart.Cart;
import com.arturoo404.NewsPage.entity.shop.cartDetail.CartDetail;
import com.arturoo404.NewsPage.entity.shop.order.Order;
import com.arturoo404.NewsPage.entity.shop.order.dto.OrderDetailDto;
import com.arturoo404.NewsPage.entity.shop.order.dto.OrderDto;
import com.arturoo404.NewsPage.entity.shop.order.dto.OrderUserListDto;
import com.arturoo404.NewsPage.entity.shop.order_adderss.OrderAddress;
import com.arturoo404.NewsPage.entity.shop.order_detail.OrderDetail;
import com.arturoo404.NewsPage.entity.shop.order_detail.dto.OrderProductDto;
import com.arturoo404.NewsPage.entity.shop.product.Product;
import com.arturoo404.NewsPage.entity.user.User;
import com.arturoo404.NewsPage.enums.OrderStatus;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.repository.UserRepository;
import com.arturoo404.NewsPage.repository.shop.OrderRepository;
import com.arturoo404.NewsPage.repository.shop.ProductRepository;
import com.arturoo404.NewsPage.service.shop.AvailableProductService;
import com.arturoo404.NewsPage.service.shop.CartService;
import com.arturoo404.NewsPage.service.shop.OrderService;
import com.arturoo404.NewsPage.validation.PhoneNumberValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final PhoneNumberValid phoneNumberValid;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final CartService cartService;

    @Autowired
    private final AvailableProductService availableProductService;

    public OrderServiceImpl(OrderRepository orderRepository, PhoneNumberValid phoneNumberValid, UserRepository userRepository, ProductRepository productRepository, CartService cartService, AvailableProductService availableProductService) {
        this.orderRepository = orderRepository;
        this.phoneNumberValid = phoneNumberValid;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
        this.availableProductService = availableProductService;
    }

    @Override
    public Object makeOrder(OrderDto orderDto) throws ExistInDatabaseException, ParseException {
        phoneNumberValid.valid(orderDto.getPhoneNumber());

        Optional<User> byEmail = userRepository.findByEmail(orderDto.getEmail());

        if (byEmail.isEmpty()){
            throw new ExistInDatabaseException("User not found.");
        }

        Cart cart = byEmail.get().getCart();

        if (cart.getProductQuantity() == 0){
            throw new ExistInDatabaseException("You do not have any product in your cart.");
        }

        final Optional<CartDetail> any = cart.getCartDetail().stream()
                .filter(a -> {
                    final Optional<Product> byId = productRepository.findById(a.getProduct().getId());
                    return byId.get().getAvailableProduct().getProductQuantity() < a.getQuantity();
                })
                .findAny();

        if (any.isPresent()){
            throw new ExistInDatabaseException("We do not have that quantity of this product:\n" + any.get().getProduct().getName());
        }


        Order order = Order.builder()
                .user(byEmail.get())
                .ordersAddress(OrderAddress.builder()
                        .firstName(orderDto.getFirstName())
                        .lastName(orderDto.getLastName())
                        .homeNumber(String.valueOf(orderDto.getHomeNumber()))
                        .postcode(orderDto.getPostcode())
                        .phoneNumber(String.valueOf(orderDto.getPhoneNumber()))
                        .city(orderDto.getCity())
                        .street(orderDto.getStreet())
                        .build())
                .orderStatus(OrderStatus.NEW)
                .orderAmount(cart.getAmount())
                .orderDate(parseDate(Date.from(Instant.now())))
                .commentsToOrder(orderDto.getCommentsToOrder())
                .build();
        order.setOrderDetails(
                cart.getCartDetail().stream()
                        .map(c -> OrderDetail.builder()
                                .orders(order)
                                .product(c.getProduct())
                                .productPrice(productPrice(c.getProduct().getId()))
                                .productQuantity(Long.valueOf(c.getQuantity()))
                                .build())
                        .collect(Collectors.toList())
        );

        orderRepository.save(order);
        availableProductService.updateProductQuantity(cart.getCartDetail());
        cartService.deleteCartDetail(cart.getId());
        cartService.restartStatistic(cart.getId());

        return "Successfully make an order";
    }

    @Override
    public List<OrderUserListDto> getUserOrderList(String email) {
        List<Order> orders = orderRepository.findAllByUserEmail(email);
        return orders.stream()
                .map(o -> OrderUserListDto.builder()
                        .id(o.getId())
                        .amount(o.getOrderAmount())
                        .orderStatus(o.getOrderStatus())
                        .date(o.getOrderDate())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public OrderDetailDto getUserOrderDetail(String email, Long id) throws ExistInDatabaseException {
        final Optional<Order> byIdAndEmail = orderRepository.findByIdAndEmail(email, id);
        if (byIdAndEmail.isEmpty()){
            throw new ExistInDatabaseException("Order not found.");
        }

        return OrderDetailDto.builder()
                .amount(byIdAndEmail.get().getOrderAmount())
                .orderStatus(byIdAndEmail.get().getOrderStatus())
                .date(byIdAndEmail.get().getOrderDate())
                .products(
                        byIdAndEmail.get().getOrderDetails().stream()
                                .map(d -> new OrderProductDto(
                                        d.getProductPrice(),
                                        d.getProductQuantity())
                                ).collect(Collectors.toList()))
                .build();
    }

    @Override
    public Page<OrderUserListDto> getPageOfOrderList(Integer page, OrderStatus orderStatus) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("id"));
        return orderRepository.findAllByOrderStatus(orderStatus, pageable)
                .map(o -> OrderUserListDto.builder()
                        .id(o.getId())
                        .amount(o.getOrderAmount())
                        .orderStatus(o.getOrderStatus())
                        .date(o.getOrderDate())
                        .build());
    }

    //TODO Email send
    @Override
    public Object updateStatus(Long orderId, OrderStatus orderStatus) {
        Optional<Order> byId = orderRepository.findById(orderId);
        if (byId.isEmpty()){
            throw new RuntimeException("Order not found.");
        }

        byId.get().setOrderStatus(orderStatus);
        return orderRepository.save(byId.get());
    }

    private Double productPrice(Long productId){
        Product product = productRepository.findById(productId).get();

        if (product.getProductPrice().isDiscount()){
            return product.getProductPrice().getDiscountPrice();
        }
        return product.getProductPrice().getPrice();
    }

    private Date parseDate(Date date) throws ParseException {
        String pattern = "yyyy-MM-dd-HH-mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String format = simpleDateFormat.format(date);

        return simpleDateFormat.parse(format);
    }
}
