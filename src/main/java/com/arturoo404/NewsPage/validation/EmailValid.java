package com.arturoo404.NewsPage.validation;

import com.arturoo404.NewsPage.exception.ValidException;

public interface EmailValid {

    void emailValid(String email) throws ValidException;
}
