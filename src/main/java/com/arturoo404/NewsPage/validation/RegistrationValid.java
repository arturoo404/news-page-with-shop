package com.arturoo404.NewsPage.validation;

import com.arturoo404.NewsPage.entity.user.dto.UserRegistrationDto;
import com.arturoo404.NewsPage.exception.ValidException;

public interface RegistrationValid {

    void registrationValid(UserRegistrationDto userRegistrationDto) throws ValidException;
}
