package com.arturoo404.NewsPage.entity.shop.product;

import com.arturoo404.NewsPage.entity.shop.available.AvailableProduct;
import com.arturoo404.NewsPage.entity.shop.cartDetail.CartDetail;
import com.arturoo404.NewsPage.entity.shop.order_detail.OrderDetail;
import com.arturoo404.NewsPage.entity.shop.price.ProductPrice;
import com.arturoo404.NewsPage.enums.ProductCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "product")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(
            nullable = false,
            name = "name",
            length = 60,
            unique = true
    )
    private String name;

    @Column(
            name = "photo",
            length = 729496729
    )
    private byte[] photo;

    @Column(
            name = "description",
            length = 5000
    )
    private String description;

    @Enumerated(
            value = EnumType.STRING
    )
    private ProductCategory productCategory;

    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "product_price",
            referencedColumnName = "id"
    )
    @JsonIgnore
    private ProductPrice productPrice;


    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "available_product",
            referencedColumnName = "id"
    )
    @JsonIgnore
    private AvailableProduct availableProduct;


    @JsonIgnore
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "product"
    )
    private List<CartDetail> cartDetail;

    @JsonIgnore
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST},
            mappedBy = "product"
    )
    private List<OrderDetail> orderDetail;
}
