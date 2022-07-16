package com.arturoo404.NewsPage.validation;

import com.arturoo404.NewsPage.exception.ValidException;

public interface NickValid {

    void nickValid(String nick) throws ValidException;
}
