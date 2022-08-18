package com.arturoo404.NewsPage.controller.api;

import com.arturoo404.NewsPage.entity.article.dto.*;
import com.arturoo404.NewsPage.entity.content.dto.ArticleContentDto;
import com.arturoo404.NewsPage.entity.photo.dto.ArticlePhotoAddDto;
import com.arturoo404.NewsPage.entity.photo.dto.PhotoDto;
import com.arturoo404.NewsPage.service.ArticleService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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

    @GetMapping(path = "/tile")
    public ResponseEntity<Page<TileArticleDto>> tilePage(@RequestParam("page") Integer page,
                                                         @RequestParam("tag") String tag){
        return ResponseEntity.status(HttpStatus.OK)
                .body(articleService.getArticleTile(page, tag));
    }

    @GetMapping(path = "/{id}/photo")
    public void articleImg(@PathVariable(name = "id") Long id, HttpServletResponse response) throws IOException {
        PhotoDto photoDto = articleService.getMainArticlePhoto(id);

        response.setContentType("image/jpeg");
        InputStream is = new ByteArrayInputStream(photoDto.getPhoto());
        IOUtils.copy(is, response.getOutputStream());
    }

    @GetMapping(path = "/content")
    public ResponseEntity<ArticlePageDataDto> content(@RequestParam("articleId") Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(articleService.getContent(id));
    }

    @GetMapping(path = "/title")
    public ResponseEntity<ArticleTitleDto> articleTitle(@RequestParam("articleId") Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(articleService.getTitle(id));
    }

    @GetMapping(path = "/photo/inside/{id}")
    public void articleInsideImg(@PathVariable(name = "id") Long id, HttpServletResponse response) throws IOException {
        PhotoDto photoDto = articleService.getArticleInsidePhoto(id);

        response.setContentType("image/jpeg");
        InputStream is = new ByteArrayInputStream(photoDto.getPhoto());
        IOUtils.copy(is, response.getOutputStream());
    }
    @GetMapping(path = "/manage/list")
    public ResponseEntity<Page<ArticleStatusListDto>> articleStatus(@RequestParam("pageNumber") Integer page){
        return ResponseEntity.status(HttpStatus.OK)
                .body(articleService.getArticleList(page));
    }

    @PostMapping(path = "/manage/status")
    public ResponseEntity<String> changeStatus(@RequestParam("articleId") Long articleId){
        articleService.changeArticleStatus(articleId);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Test");
    }
}
