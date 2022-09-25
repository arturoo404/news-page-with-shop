package com.arturoo404.NewsPage.entity.shop.order_detail;

import com.arturoo404.NewsPage.entity.shop.order.Order;
import com.arturoo404.NewsPage.entity.shop.product.Product;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_detail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "product_price",
            nullable = false
    )
    private Double productPrice;

    @Column(
            name = "product_quantity",
            nullable = false
    )
    private Long productQuantity;

    @ManyToOne
    @JoinColumn(
            name = "orders"
    )
    private Order orders;

    @ManyToOne
    @JoinColumn(
            name = "product"
    )
    private Product product;
}
