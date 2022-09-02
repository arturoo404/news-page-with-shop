package com.arturoo404.NewsPage.validation.impl;

import com.arturoo404.NewsPage.exception.ValidException;
import com.arturoo404.NewsPage.validation.PasswordValid;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PasswordValidImpl implements PasswordValid {

    private final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";

    private final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    @Override
    public void password(String password, String confirmPassword) throws ValidException {
        Matcher matcher = pattern.matcher(password);
        if (!password.equals(confirmPassword)) {
            throw new ValidException("Passwords did not match.");
        }
        if (!matcher.matches()){
            throw new ValidException("Password strength is to low.");
        }
    }
}
