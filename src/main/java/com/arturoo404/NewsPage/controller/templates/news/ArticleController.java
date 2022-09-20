package com.arturoo404.NewsPage.controller.templates.news;

import com.arturoo404.NewsPage.service.news.ArticleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/article")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping()
    public String articleList(@RequestParam("tag") String tag){
        return "article/list";
    }
    @GetMapping(path = "/create")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JOURNALIST')")
    public String createArticle(){
        return "add/article";
    }

    @GetMapping(path = "/photo")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JOURNALIST')")
    public String addPhotoForArticle(@RequestParam(name = "articleId") Long id){
        return "article/photo_add";
    }

    @GetMapping(path = "/detail")
    public String articleDetail(@RequestParam(name = "articleId") Long id){
        return "article/detail";
    }

    @GetMapping(path = "/manage")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'JOURNALIST')")
    public String manageArticle(){
        return "article/manage";
    }

    @GetMapping("/search")
    public String articleSearch(){
        return "article/search";
    }
}
