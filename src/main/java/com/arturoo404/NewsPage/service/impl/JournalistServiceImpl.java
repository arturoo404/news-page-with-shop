package com.arturoo404.NewsPage.service.impl;

import com.arturoo404.NewsPage.repository.JournalistRepository;
import com.arturoo404.NewsPage.service.JournalistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JournalistServiceImpl implements JournalistService {

    private final JournalistRepository journalistRepository;

    @Autowired
    public JournalistServiceImpl(JournalistRepository journalistRepository) {
        this.journalistRepository = journalistRepository;
    }
}
