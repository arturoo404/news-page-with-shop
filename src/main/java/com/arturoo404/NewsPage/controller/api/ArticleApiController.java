package com.arturoo404.NewsPage.controller.api;

import com.arturoo404.NewsPage.entity.article.dto.CreateArticleDto;
import com.arturoo404.NewsPage.entity.article.dto.TileArticleDto;
import com.arturoo404.NewsPage.entity.photo.dto.ArticlePhotoAddDto;
import com.arturoo404.NewsPage.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = "/api/article")
public class ArticleApiController {

    private final ArticleService articleService;

    @Autowired
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

    @GetMapping(path = "/photo/content/{id}")
    public ResponseEntity<Integer> contentForAddPhoto(@PathVariable(name = "id") Long articleId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(articleService.getNumberOfPhotos(articleId));
    }

    @PostMapping(path = "/photo/parameter")
    public ResponseEntity<Object> savePhotoStatistic(@RequestBody ArticlePhotoAddDto addDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleService.saveArticleStatistic(addDto));
    }

    @PostMapping(path = "/photo/inside")
    public ResponseEntity<Object> addPhotoInsideArticle(@RequestParam("file") MultipartFile photo,
                                                        @RequestParam("articleId") Long id,
                                                        @RequestParam("position") Integer position) throws IOException {
        articleService.savePhotoInsideArticle(photo, id, position);
        return ResponseEntity
                .ok("Photo uploaded successfully.");
    }

    @GetMapping(path = "/tile/page/{page}")
    public ResponseEntity<Page<TileArticleDto>> tilePage(@PathVariable(name = "page") Integer page,
                                                         @RequestParam("tag") String tag){
        return ResponseEntity.status(HttpStatus.OK)
                .body(articleService.getArticleTile(page, tag));
    }
}
