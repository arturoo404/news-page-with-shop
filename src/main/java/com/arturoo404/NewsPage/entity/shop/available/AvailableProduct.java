package com.arturoo404.NewsPage.entity.shop.available;

import com.arturoo404.NewsPage.entity.shop.product.Product;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "available_product")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AvailableProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "available_status"
    )
    private boolean availableStatus;

    @Column(
            name = "product_quantity"
    )
    private Integer productQuantity;

    @OneToOne(mappedBy = "availableProduct")
    private Product product;
}
