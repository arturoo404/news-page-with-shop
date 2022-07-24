package com.arturoo404.NewsPage.service;

import com.arturoo404.NewsPage.entity.journalist.Journalist;
import com.arturoo404.NewsPage.entity.journalist.dto.JournalistAddDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;

public interface JournalistService {
    Journalist addJournalist(JournalistAddDto journalistAddDto) throws ExistInDatabaseException;
}
