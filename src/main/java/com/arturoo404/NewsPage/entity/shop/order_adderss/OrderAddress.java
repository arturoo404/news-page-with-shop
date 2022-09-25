package com.arturoo404.NewsPage.entity.shop.order_adderss;

import com.arturoo404.NewsPage.entity.shop.order.Order;
import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_address")
public class OrderAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(
            length = 60,
            nullable = false
    )
    private String firstName;

    @Column(
            length = 60,
            nullable = false
    )
    private String lastName;

    @Column(
            length = 50,
            nullable = false
    )
    private String city;

    @Column(
            length = 10,
            nullable = false
    )
    private String postcode;
    @Column(
            length = 20,
            nullable = false
    )
    private String street;
    @Column(
            length = 10,
            nullable = false
    )
    private String homeNumber;
    @Column(
            length = 20,
            nullable = false
    )
    private String phoneNumber;

    @OneToOne(
            mappedBy = "ordersAddress"
    )
    private Order order;

}
