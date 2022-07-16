package com.arturoo404.NewsPage.validation.impl;

import com.arturoo404.NewsPage.entity.user.User;
import com.arturoo404.NewsPage.exception.ValidException;
import com.arturoo404.NewsPage.repository.UserRepository;
import com.arturoo404.NewsPage.validation.NickValid;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NickValidImpl implements NickValid {

    private final UserRepository userRepository;

    public NickValidImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void nickValid(String nick) throws ValidException {
        Optional<User> user = userRepository.findByNick(nick);
        if (user.isPresent()){
            throw new ValidException("Nick is already taken");
        }
    }
}
