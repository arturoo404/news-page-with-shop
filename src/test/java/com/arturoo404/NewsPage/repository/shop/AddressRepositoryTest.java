package com.arturoo404.NewsPage.repository.shop;

import com.arturoo404.NewsPage.entity.shop.address.Address;
import com.arturoo404.NewsPage.entity.user.User;
import com.arturoo404.NewsPage.enums.UserRole;
import com.arturoo404.NewsPage.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AddressRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Test
    void findAddressByUserEmail() {
        //Given
        final User user = userRepository.save(User.builder()
                .email("email")
                .nick("nick")
                .userRole(UserRole.USER)
                        .address(Address.builder()
                                .street("street")
                                .phoneNumber("12345677")
                                .postcode("22222")
                                .homeNumber("10")
                                .city("city")
                                .firstName("first")
                                .lastName("last")
                                .build())
                .password(
                        new BCryptPasswordEncoder()
                                .encode("test")
                ).build());



        //When
        final Optional<Address> addressByUserEmail = addressRepository.findAddressByUserEmail(user.getEmail());

        //Then
        assertThat(addressByUserEmail).isPresent();
    }

}