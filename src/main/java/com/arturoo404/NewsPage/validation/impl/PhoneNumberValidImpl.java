package com.arturoo404.NewsPage.validation.impl;

import com.arturoo404.NewsPage.validation.PhoneNumberValid;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PhoneNumberValidImpl implements PhoneNumberValid {

    private static final String regex = "^[1-9]\\d{9,14}";

    @Override
    public boolean valid(Long number) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(String.valueOf(number));
        return matcher.matches();
    }
}
