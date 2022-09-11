package com.arturoo404.NewsPage.entity.user.dto;

import com.arturoo404.NewsPage.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserChangeRoleDto {
    private String email;
    private UserRole role;
}
