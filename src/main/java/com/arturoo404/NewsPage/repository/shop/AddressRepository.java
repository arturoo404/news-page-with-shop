package com.arturoo404.NewsPage.repository.shop;

import com.arturoo404.NewsPage.entity.shop.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
