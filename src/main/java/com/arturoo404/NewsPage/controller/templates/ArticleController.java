package com.arturoo404.NewsPage.controller.templates;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/article")
public class ArticleController {

    @GetMapping()
    public String articleList(@RequestParam("tag") String tag){
        return "article/list";
    }
    @GetMapping(path = "/create")
    public String createArticle(){
        return "add/article";
    }

    @GetMapping(path = "/photo")
    public String addPhotoForArticle(@RequestParam(name = "articleId") Long id){
        return "article/photo_add";
    }

    @GetMapping(path = "/detail")
    public String articleDetail(@RequestParam(name = "articleId") Long id){
        return "article/detail";
    }
}
