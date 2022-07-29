package com.arturoo404.NewsPage.controller.templates;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/journalist")
public class JournalistController {

    @GetMapping(path = "/create")
    public String createJournalist(){
        return "add/journalist";
    }
}
