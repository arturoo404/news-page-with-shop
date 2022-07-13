package com.arturoo404.NewsPage.controller.templates;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class BasicPageController {

    @GetMapping(path = "/")
    private String homePage(){
        return "basic/home";
    }
}
