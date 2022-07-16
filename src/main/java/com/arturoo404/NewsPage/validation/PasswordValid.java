package com.arturoo404.NewsPage.validation;

import com.arturoo404.NewsPage.exception.ValidException;

public interface PasswordValid {

    void password(String password, String confirmPassword) throws ValidException;
}
