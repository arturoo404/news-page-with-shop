package com.arturoo404.NewsPage.controller.api;

import com.arturoo404.NewsPage.entity.article.dto.*;
import com.arturoo404.NewsPage.entity.photo.dto.ArticlePhotoAddDto;
import com.arturoo404.NewsPage.entity.photo.dto.PhotoDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.service.ArticleService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JOURNALIST')")
    ResponseEntity<Object> addArticle(@RequestBody CreateArticleDto createArticleDto){
        if (createArticleDto.getContent().isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Content field can not be blank.");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleService.addArticle(createArticleDto));
    }

    @PostMapping(path = "/photo/add/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JOURNALIST')")
    public ResponseEntity<String> addArticlePhoto(@RequestParam("file") MultipartFile photo,
                                                  @PathVariable("id") Long id) throws IOException {
        if (photo.getSize() == 0){
            articleService.deleteArticle(id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("You did not chose photo.");
        }
        articleService.addArticlePhoto(photo, id);
        return ResponseEntity
                .ok("Photo uploaded successfully.");
    }

    @DeleteMapping(path = "/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JOURNALIST')")
    public ResponseEntity<String> deleteArticle(@PathVariable("id") Long id){
        articleService.deleteArticle(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Successfully delete article.");
    }
    @GetMapping(path = "/photo/content/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JOURNALIST')")
    public ResponseEntity<Integer> getTotalNumberOfPhotosInArticle(@PathVariable(name = "id") Long articleId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(articleService.getNumberOfPhotos(articleId));
    }

    @PostMapping(path = "/photo/parameter")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JOURNALIST')")
    public ResponseEntity<Object> savePhotoStatistic(@RequestBody ArticlePhotoAddDto addDto){
        if (addDto.getPhotoPosition() < 1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Select your photo position.");
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleService.saveArticleStatistic(addDto));
    }

    @PostMapping(path = "/photo/inside")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JOURNALIST')")
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

    //TODO Search article test
    @GetMapping(path = "/search")
    public ResponseEntity<Page<TileArticleDto>> tilePageByKeyword(@RequestParam("page") Integer page,
                                                         @RequestParam("keyword") String keyword){
        return ResponseEntity.status(HttpStatus.OK)
                .body(articleService.getArticleTileByKeyword(page, keyword));
    }

    @GetMapping(path = "/{id}/photo")
    public void articleImg(@PathVariable(name = "id") Long id, HttpServletResponse response) throws IOException {
        PhotoDto photoDto = articleService.getMainArticlePhoto(id);

        response.setContentType("image/jpeg");
        InputStream is = new ByteArrayInputStream(photoDto.getPhoto());
        IOUtils.copy(is, response.getOutputStream());
    }

    //TODO Article content test
    @GetMapping(path = "/content")
    public ResponseEntity<Object> content(@RequestParam("articleId") Long id){
        try {
            final ResponseEntity<Object> body = ResponseEntity.status(HttpStatus.OK)
                    .body(articleService.getContent(id));

            articleService.changeArticlePopularity(id);
            return body;
        } catch (ExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
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
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JOURNALIST')")
    public ResponseEntity<Page<ArticleStatusListDto>> articleStatus(@RequestParam("pageNumber") Integer page){
        return ResponseEntity.status(HttpStatus.OK)
                .body(articleService.getArticleList(page));
    }

    @PostMapping(path = "/manage/status")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JOURNALIST')")
    public ResponseEntity<String> changeStatus(@RequestParam("articleId") Long articleId){
        articleService.changeArticleStatus(articleId);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Status has changed.");
    }

    //TODO Last published test
    @GetMapping(path = "/last-published")
    public ResponseEntity<?> lastPublishedArticle(){
        return ResponseEntity.ok(
                articleService.getLastPublishedArticleList()
        );
    }

    //TODO Last published by tag test
    @GetMapping(path = "/last-published/{tag}")
    public ResponseEntity<?> lastPublishedArticleByTag(@PathVariable(name = "tag") String tag){
        return ResponseEntity.ok(
                articleService.lastPublishedArticleByTagList(tag)
        );
    }

    //TODO Popularity article test
    @GetMapping(path = "/popularity")
    public ResponseEntity<?> popularityArticle(){
        return ResponseEntity.ok(
                articleService.getPopularityArticle()
        );
    }
}
