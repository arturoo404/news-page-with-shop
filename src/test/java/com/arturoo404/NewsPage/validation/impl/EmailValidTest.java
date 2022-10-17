package com.arturoo404.NewsPage.validation.impl;

import com.arturoo404.NewsPage.entity.user_objects.user.User;
import com.arturoo404.NewsPage.exception.ValidException;
import com.arturoo404.NewsPage.repository.user.UserRepository;
import com.arturoo404.NewsPage.validation.EmailValid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class EmailValidTest {

    private EmailValid emailValid;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        emailValid = new EmailValidImpl(userRepository);
    }

    @Test
    void itShouldThrowSyntaxException() {
        //Given
        String email = "email";

        //When
        try {
            emailValid.emailValid(email);
        } catch (ValidException e) {
            assertThat(e.getMessage()).isEqualTo("Bad email syntax.");
        }
        //Then
    }

    @Test
    void itShouldThrowEmailTakenException() {
        //Given
        String email = "email@gmail.com";
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(new User()));

        //When
        try {
            emailValid.emailValid(email);
        } catch (ValidException e) {
            assertThat(e.getMessage()).isEqualTo("This email is already taken.");
        }
        //Then
    }

    @Test
    void itShouldNotThrowAnyException() {
        //Given
        String email = "email@gmail.com";
        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

        //When
        try {
            emailValid.emailValid(email);
        } catch (ValidException e) {
            assertThat(e).doesNotThrowAnyException();
        }
        //Then
    }
}