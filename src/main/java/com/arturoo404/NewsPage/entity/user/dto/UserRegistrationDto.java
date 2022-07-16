package com.arturoo404.NewsPage.entity.user.dto;
import lombok.Getter;

@Getter
public class UserRegistrationDto {
    private String email;
    private String nick;
    private String password;
    private String passwordConfirm;
}
