package com.arturoo404.NewsPage.validation.impl;

import com.arturoo404.NewsPage.entity.user_objects.user.User;
import com.arturoo404.NewsPage.exception.ValidException;
import com.arturoo404.NewsPage.repository.user.UserRepository;
import com.arturoo404.NewsPage.validation.NickValid;
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
class NickValidTest {

    private NickValid nickValid;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        nickValid = new NickValidImpl(userRepository);
    }

    @Test
    void itShouldThrowNickTakenException() {
        //Given
        given(userRepository.findByNick(anyString())).willReturn(Optional.of(User.builder()
                        .nick("nick")
                .build()));
        String nick = "nick";

        //When
        try {
            nickValid.nickValid(nick);
        } catch (ValidException e) {
            assertThat(e.getMessage()).isEqualTo("Nick is already taken.");
        }
    }

    @Test
    void itShouldNotThrowAnyException() {
        //Given
        given(userRepository.findByNick(anyString())).willReturn(Optional.empty());
        String nick = "nick";

        //When
        try {
            nickValid.nickValid(nick);
        } catch (ValidException e) {
            assertThat(e.getMessage()).isEqualTo("Nick is already taken.");
        }
    }
}