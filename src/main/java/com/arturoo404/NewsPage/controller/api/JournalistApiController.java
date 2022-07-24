package com.arturoo404.NewsPage.controller.api;

import com.arturoo404.NewsPage.entity.journalist.Journalist;
import com.arturoo404.NewsPage.entity.journalist.dto.JournalistAddDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.service.JournalistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/api/journalist")
public class JournalistApiController {

    private final JournalistService journalistService;

    public JournalistApiController(JournalistService journalistService) {
        this.journalistService = journalistService;
    }

    @PostMapping(path = "/add")
    public ResponseEntity<Object> userRegistration(@RequestBody JournalistAddDto journalistAddDto){
        if (journalistAddDto.getName().isEmpty() || journalistAddDto.getInfo().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(journalistAddDto);
        }
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(journalistService.addJournalist(journalistAddDto));
        } catch (ExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
