package com.arturoo404.NewsPage.entity.shop.order;

import com.arturoo404.NewsPage.enums.OrderStatus;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order")
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
}
