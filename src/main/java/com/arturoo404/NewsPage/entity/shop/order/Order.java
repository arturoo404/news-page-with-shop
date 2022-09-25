package com.arturoo404.NewsPage.entity.shop.order;

import com.arturoo404.NewsPage.entity.shop.order_detail.OrderDetail;
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

    @JsonIgnore
    @OneToMany(
            mappedBy = "orders",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<OrderDetail> orderDetails;

}
