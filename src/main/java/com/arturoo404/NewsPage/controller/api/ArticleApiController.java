package com.arturoo404.NewsPage.controller.api;

import com.arturoo404.NewsPage.entity.article.dto.CreateArticleDto;
import com.arturoo404.NewsPage.entity.journalist.dto.JournalistGetDto;
import com.arturoo404.NewsPage.entity.photo.dto.ArticlePhotoAddDto;
import com.arturoo404.NewsPage.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/article")
public class ArticleApiController {

    private final ArticleService articleService;

    public ArticleApiController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping(path = "/add")
    ResponseEntity<Object> addArticle(@RequestBody CreateArticleDto createArticleDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleService.addArticle(createArticleDto));
    }

    @PostMapping(path = "/photo/add/{id}")
    public ResponseEntity<Object> addArticlePhoto(@RequestParam("file") MultipartFile photo,
                                                     @PathVariable("id") Long id) throws IOException {
        articleService.addArticlePhoto(photo, id);
        return ResponseEntity
                .ok("Photo uploaded successfully.");
    }

    @GetMapping("/photo/content/{id}")
    public ResponseEntity<Integer> contentForAddPhoto(@PathVariable(name = "id") Long articleId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(articleService.getNumberOfPhotos(articleId));
    }

    @PostMapping
    public ResponseEntity<Object> savePhotoStatistic(@RequestBody ArticlePhotoAddDto addDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleService.saveArticleStatistic(addDto));
    }
}
