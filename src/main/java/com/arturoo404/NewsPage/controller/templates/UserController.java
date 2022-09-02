package com.arturoo404.NewsPage.controller.templates;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    @GetMapping(path = "/manage")
    @PreAuthorize("isAuthenticated()")
    public String changePassword(){
        return "user/manage_account";
    }
}
