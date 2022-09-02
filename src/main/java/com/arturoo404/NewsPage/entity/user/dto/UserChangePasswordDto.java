package com.arturoo404.NewsPage.entity.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserChangePasswordDto {

    private String newPassword;
    private String confirmPassword;
    private String oldPassword;
    private String account;
}
