package com.arturoo404.NewsPage.service;

import com.arturoo404.NewsPage.entity.journalist.Journalist;
import com.arturoo404.NewsPage.entity.journalist.dto.JournalistAddDto;
import com.arturoo404.NewsPage.entity.journalist.dto.JournalistGetDto;
import com.arturoo404.NewsPage.exception.EmptyValueException;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface JournalistService {
    Journalist addJournalist(JournalistAddDto journalistAddDto) throws ExistInDatabaseException;

    void addJournalistPhoto(MultipartFile photo, Short id) throws IOException;

    List<JournalistGetDto> getJournalistList();
}
