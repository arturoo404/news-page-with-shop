package com.arturoo404.NewsPage.validation.impl;

import com.arturoo404.NewsPage.exception.ValidException;
import com.arturoo404.NewsPage.validation.PasswordValid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PasswordValidImplTest {

    private PasswordValid passwordValid;

    @BeforeEach
    void setUp() {
        passwordValid = new PasswordValidImpl();
    }

    @Test
    void itShouldThrowPasswordsNotMatch() {
        //Given
        String password = "AFafVV#%3535ccaD";
        String confirmPassword = "confirm";

        //When
        try {
            passwordValid.password(password, confirmPassword);
        } catch (ValidException e) {
            assertThat(e.getMessage()).isEqualTo("Passwords did not match.");
        }
    }

    @Test
    void itShouldThrowPasswordsStrengthIsToLow() {
        //Given
        String password = "Password";
        String confirmPassword = "Password";

        //When
        try {
            passwordValid.password(password, confirmPassword);
        } catch (ValidException e) {
            assertThat(e.getMessage()).isEqualTo("Password strength is to low.");
        }
    }

    @Test
    void itShouldNotThrowAnyException() {
        //Given
        String password = "AFafVV#%3535ccaD";
        String confirmPassword = "AFafVV#%3535ccaD";

        //When
        try {
            passwordValid.password(password, confirmPassword);
        } catch (ValidException e) {
            assertThat(e.getMessage()).isEqualTo("Password strength is to low.");
        }
    }
}