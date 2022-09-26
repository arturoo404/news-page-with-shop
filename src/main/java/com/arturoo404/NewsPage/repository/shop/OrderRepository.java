package com.arturoo404.NewsPage.repository.shop;

import com.arturoo404.NewsPage.entity.shop.order.Order;
import com.arturoo404.NewsPage.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserEmail(String email);

    @Query(value = "FROM Order o WHERE  o.user.email = ?1 AND o.id = ?2")
    Optional<Order> findByIdAndEmail(String email, Long id);

    @Query(value = "FROM Order o WHERE o.orderStatus = ?1")
    Page<Order> findAllByOrderStatus(OrderStatus orderStatus, Pageable pageable);
}
