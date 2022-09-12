package com.arturoo404.NewsPage.entity.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserChangePasswordDto {

    private String newPassword;
    private String confirmPassword;
    private String oldPassword;
    private String account;
}
