package com.arturoo404.NewsPage.service;

import com.arturoo404.NewsPage.entity.user.User;
import com.arturoo404.NewsPage.entity.user.dto.UserChangePasswordDto;
import com.arturoo404.NewsPage.entity.user.dto.UserChangeRoleDto;
import com.arturoo404.NewsPage.entity.user.dto.UserRegistrationDto;
import com.arturoo404.NewsPage.enums.UserRole;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.exception.ValidException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    User registerUser(UserRegistrationDto userRegistrationDto) throws ValidException;

    User changePassword(UserChangePasswordDto user) throws ExistInDatabaseException, ValidException;

    void changeUserRole(UserChangeRoleDto userChangeRoleDto) throws ExistInDatabaseException;

    Object findCurrentRole(String email) throws ExistInDatabaseException;
}
