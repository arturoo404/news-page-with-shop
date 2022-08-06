package com.arturoo404.NewsPage.controller.templates;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/article")
public class ArticleController {

    @GetMapping(path = "/create")
    public String createArticle(){
        return "add/article";
    }

    @GetMapping(path = "/photo/{id}")
    public String addPhotoForArticle(@PathVariable Long id, Model model){
        model.addAttribute("id", id);
        return "article/photo_add";
    }
}
