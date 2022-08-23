package com.arturoo404.NewsPage.controller.api;

import com.arturoo404.NewsPage.entity.journalist.dto.JournalistAddDto;
import com.arturoo404.NewsPage.entity.journalist.dto.JournalistGetDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.service.JournalistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/journalist")
public class JournalistApiController {

    private final JournalistService journalistService;

    public JournalistApiController(JournalistService journalistService) {
        this.journalistService = journalistService;
    }

    @PostMapping(path = "/add")
    @PreAuthorize("hasAuthority('ADMIN')")
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

    @PostMapping(path = "/photo/add/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> addJournalistPhoto(@RequestParam("file") MultipartFile photo,
                                                     @PathVariable(value = "id") Short id) throws IOException {
        journalistService.addJournalistPhoto(photo, id);
        return ResponseEntity
                .ok("Photo uploaded successfully.");
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<JournalistGetDto>> getJournalistList(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(journalistService.getJournalistList());
    }
}
