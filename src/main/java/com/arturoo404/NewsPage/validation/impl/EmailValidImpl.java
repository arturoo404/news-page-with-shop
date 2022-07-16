package com.arturoo404.NewsPage.validation.impl;

import com.arturoo404.NewsPage.exception.ValidException;
import com.arturoo404.NewsPage.repository.UserRepository;
import com.arturoo404.NewsPage.validation.EmailValid;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailValidImpl implements EmailValid{

    private final UserRepository userRepository;
    private final String EMAIL_PATTERN =
            "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    private final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public EmailValidImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void emailValid(String email) throws ValidException {
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()){
            throw new ValidException("Bad email");
        }
        if (!userRepository.findByEmail(email).isEmpty()){
            throw new ValidException("This email is already taken");
        }
    }
}
