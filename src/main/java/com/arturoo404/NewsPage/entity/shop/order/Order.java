package com.arturoo404.NewsPage.entity.shop.order;

import com.arturoo404.NewsPage.entity.shop.order_adderss.OrderAddress;
import com.arturoo404.NewsPage.entity.shop.order_detail.OrderDetail;
import com.arturoo404.NewsPage.entity.user.User;
import com.arturoo404.NewsPage.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(
            value = EnumType.STRING
    )
    @Column(
            name = "order_status",
            nullable = false
    )
    private OrderStatus orderStatus;

    @Column(
            name = "order_amount",
            nullable = false
    )
    private Double orderAmount;

    @Column(
            name = "order_date"
    )
    private Date orderDate;

    @Column(
            name = "comments_to_order",
            length = 2000
    )
    private String commentsToOrder;


    @JsonIgnore
    @OneToMany(
            mappedBy = "orders",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<OrderDetail> orderDetails;


    @ManyToOne()
    @JoinColumn(
            name = "user"
    )
    private User user;

    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "orders_address",
            nullable = false
    )
    private OrderAddress ordersAddress;

}
