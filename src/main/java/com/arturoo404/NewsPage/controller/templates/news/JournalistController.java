package com.arturoo404.NewsPage.controller.templates.news;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/journalist")
public class JournalistController {

    @GetMapping(path = "/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String createJournalist(){
        return "add/journalist";
    }
}
