package com.arturoo404.NewsPage.repository;

import com.arturoo404.NewsPage.entity.user_objects.user.User;
import com.arturoo404.NewsPage.enums.UserRole;
import com.arturoo404.NewsPage.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private final String email = "test@gmail.com";

    private final String nick = "nick";

    @BeforeEach
    void setUp() {
        userRepository.save(User.builder()
                .email(email)
                .nick(nick)
                .userRole(UserRole.USER)
                .password(
                        new BCryptPasswordEncoder()
                                .encode("test")
                ).build());
    }

    @Test
    void itShouldFindOptionalUserByEmail() {
        //Given

        //When
        final Optional<User> byEmail = userRepository.findByEmail(email);

        //Then
        assertThat(byEmail).isPresent();
        assertThat(byEmail.get().getNick()).isEqualTo(nick);
    }

    @Test
    void itShouldFindOptionalUserByNick() {
        //Given

        //When
        final Optional<User> byNick = userRepository.findByNick(nick);

        //Then
        assertThat(byNick).isPresent();
        assertThat(byNick.get().getEmail()).isEqualTo(email);
    }

    @Test
    void itShouldFindUserByEmail() {
        //Given

        //When
        final User userByEmail = userRepository.findUserByEmail(email);

        //Then
        assertThat(userByEmail).isNotNull();
        assertThat(userByEmail.getNick()).isEqualTo(nick);
    }
}