package com.arturoo404.NewsPage.validation.impl;

import com.arturoo404.NewsPage.entity.user.dto.UserRegistrationDto;
import com.arturoo404.NewsPage.exception.ValidException;
import com.arturoo404.NewsPage.validation.EmailValid;
import com.arturoo404.NewsPage.validation.NickValid;
import com.arturoo404.NewsPage.validation.PasswordValid;
import com.arturoo404.NewsPage.validation.RegistrationValid;
import org.springframework.stereotype.Service;

@Service
public class RegistrationValidImpl implements RegistrationValid {

    private final EmailValid emailValid;

    private final NickValid nickValid;

    private final PasswordValid passwordValid;

    public RegistrationValidImpl(EmailValid emailValid, NickValid nickValid, PasswordValid passwordValid) {
        this.emailValid = emailValid;
        this.nickValid = nickValid;
        this.passwordValid = passwordValid;
    }


    @Override
    public void registrationValid(UserRegistrationDto userRegistrationDto) throws ValidException {
        emailValid.emailValid(userRegistrationDto.getEmail());
        nickValid.nickValid(userRegistrationDto.getNick());
        passwordValid.password(userRegistrationDto.getPassword(), userRegistrationDto.getPasswordConfirm());
    }
}
