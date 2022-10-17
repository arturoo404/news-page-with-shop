package com.arturoo404.NewsPage.service.user;

import com.arturoo404.NewsPage.entity.user_objects.user.User;
import com.arturoo404.NewsPage.entity.user_objects.user.dto.UserChangePasswordDto;
import com.arturoo404.NewsPage.entity.user_objects.user.dto.UserChangeRoleDto;
import com.arturoo404.NewsPage.entity.user_objects.user.dto.UserRegistrationDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.exception.PermissionException;
import com.arturoo404.NewsPage.exception.ValidException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.mail.MessagingException;

public interface UserService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    User registerUser(UserRegistrationDto userRegistrationDto) throws ValidException, MessagingException;

    User changePassword(UserChangePasswordDto user) throws ExistInDatabaseException, ValidException;

    void changeUserRole(UserChangeRoleDto userChangeRoleDto) throws ExistInDatabaseException;

    Object findCurrentRole(String email) throws ExistInDatabaseException;

    void blockUserAccount(Long id) throws ExistInDatabaseException, PermissionException;
}
