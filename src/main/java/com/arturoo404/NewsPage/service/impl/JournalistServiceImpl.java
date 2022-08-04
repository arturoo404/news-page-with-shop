package com.arturoo404.NewsPage.service.impl;

import com.arturoo404.NewsPage.entity.journalist.Journalist;
import com.arturoo404.NewsPage.entity.journalist.dto.JournalistAddDto;
import com.arturoo404.NewsPage.entity.journalist.dto.JournalistGetDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.repository.JournalistRepository;
import com.arturoo404.NewsPage.service.JournalistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JournalistServiceImpl implements JournalistService {

    private final JournalistRepository journalistRepository;

    @Autowired
    public JournalistServiceImpl(JournalistRepository journalistRepository) {
        this.journalistRepository = journalistRepository;
    }

    @Override
    public Journalist addJournalist(JournalistAddDto journalistAddDto) throws ExistInDatabaseException {
        Optional<Journalist> journalist = journalistRepository.findByName(journalistAddDto.getName());
        if (journalist.isPresent()){
            throw new ExistInDatabaseException("This journalist exist");
        }
        return journalistRepository.save(
                Journalist.builder()
                        .name(journalistAddDto.getName())
                        .info(journalistAddDto.getInfo())
                        .build()
        );
    }

    @Override
    public void addJournalistPhoto(MultipartFile photo, Short id) throws IOException {
        Journalist journalist = journalistRepository.findJournalistById(id);
        journalist.setPhoto(photo.getBytes());
        journalistRepository.save(journalist);
    }

    @Override
    public List<JournalistGetDto> getJournalistList() {
        return journalistRepository.findAll().stream()
                .map(m -> new JournalistGetDto(m.getId(), m.getName()))
                .collect(Collectors.toList());
    }
}
