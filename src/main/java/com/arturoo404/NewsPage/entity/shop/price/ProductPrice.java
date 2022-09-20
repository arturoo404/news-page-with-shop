package com.arturoo404.NewsPage.entity.shop.price;

import com.arturoo404.NewsPage.entity.shop.product.Product;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "product_price")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id"
    )
    private Long id;

    @Column(
            name = "price",
            nullable = false
    )
    private Double price;

    @Column(
            name = "discount",
            nullable = false
    )
    private boolean discount;

    @Column(
            name = "discount_price"
    )
    private Double discountPrice;

    @OneToOne(mappedBy = "productPrice")
    private Product product;

}
