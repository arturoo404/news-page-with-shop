package com.arturoo404.NewsPage.service.impl;

import com.arturoo404.NewsPage.entity.user.User;
import com.arturoo404.NewsPage.entity.user.dto.UserRegistrationDto;
import com.arturoo404.NewsPage.enums.UserRole;
import com.arturoo404.NewsPage.exception.ValidException;
import com.arturoo404.NewsPage.repository.UserRepository;
import com.arturoo404.NewsPage.service.UserService;
import com.arturoo404.NewsPage.validation.RegistrationValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RegistrationValid registrationValid;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RegistrationValid registrationValid) {
        this.userRepository = userRepository;
        this.registrationValid = registrationValid;
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("We can not find this email")
        );
    }

    @Override
    public User registerUser(UserRegistrationDto userDto) throws ValidException {
        registrationValid.registrationValid(userDto);
        return userRepository.save(User.builder()
                        .email(userDto.getEmail())
                        .nick(userDto.getNick())
                        .userRole(UserRole.USER)
                        .password(
                                new BCryptPasswordEncoder()
                                        .encode(userDto.getPassword())
                        )
                        .build()
                );
    }
}
