package com.arturoo404.NewsPage.entity.shop.order.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderDto {

    private String firstName;
    private String lastName;
    private String city;
    private String postcode;
    private String street;
    private Integer homeNumber;
    private Long phoneNumber;
    private String email;
    private String commentsToOrder;

}
