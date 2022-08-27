package com.arturoo404.NewsPage.service.impl;

import com.arturoo404.NewsPage.entity.user.User;
import com.arturoo404.NewsPage.entity.user.dto.UserRegistrationDto;
import com.arturoo404.NewsPage.enums.UserRole;
import com.arturoo404.NewsPage.exception.ValidException;
import com.arturoo404.NewsPage.repository.UserRepository;
import com.arturoo404.NewsPage.service.UserService;
import com.arturoo404.NewsPage.validation.RegistrationValid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RegistrationValid valid;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, valid);
    }

    @Test
    void itShouldLoadUserByUsername(){
        //Given
        String email = "test@gmail.com";

        User user = User.builder()
                .userRole(UserRole.USER)
                .nick("account")
                .password("password")
                .enabled(true)
                .locked(false)
                .email(email)
                .build();

        //When
        when(userRepository.findByEmail(email)).thenReturn(Optional.ofNullable(user));
        final UserDetails userDetails = userService.loadUserByUsername(email);

        //Then
        assertThat(userDetails.getUsername()).isEqualTo(email);
    }

    @Test
    void registerUserBadEmail() {
        //Given
        User user = new User();

        UserRegistrationDto userDto = UserRegistrationDto.builder()
                .nick("account")
                .password("password")
                .passwordConfirm("pasword")
                .email("emailgmail.com")
                .build();
        //When
        when(userRepository.save(any(User.class))).thenReturn(User.builder()
                        .userRole(UserRole.USER)
                        .nick(userDto.getNick())
                        .id(1L)
                        .email(userDto.getEmail())
                        .password(userDto.getPassword())
                .build());
        try {
           user = userService.registerUser(userDto);
        } catch (ValidException e) {
            assertThat(e.getMessage()).isNotBlank();
        }

        //Then
        assertThat(user.getUserRole()).isEqualTo(UserRole.USER);
        assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
    }
}