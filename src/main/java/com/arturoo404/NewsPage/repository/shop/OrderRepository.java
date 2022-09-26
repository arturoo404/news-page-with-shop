package com.arturoo404.NewsPage.repository.shop;

import com.arturoo404.NewsPage.entity.shop.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserEmail(String email);
}
