package com.arturoo404.NewsPage.repository.shop;

import com.arturoo404.NewsPage.entity.shop.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query(value = "FROM Address a WHERE a.user.email = ?1")
    Optional<Address> findByUserEmail(String email);
}
