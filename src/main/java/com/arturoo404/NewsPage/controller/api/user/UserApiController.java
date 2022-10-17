package com.arturoo404.NewsPage.controller.api.user;

import com.arturoo404.NewsPage.entity.user_objects.user.User;
import com.arturoo404.NewsPage.entity.user_objects.user.dto.UserChangePasswordDto;
import com.arturoo404.NewsPage.entity.user_objects.user.dto.UserChangeRoleDto;
import com.arturoo404.NewsPage.entity.user_objects.user.dto.UserRegistrationDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.exception.PermissionException;
import com.arturoo404.NewsPage.exception.ValidException;
import com.arturoo404.NewsPage.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(path = "/api/user")
public class UserApiController {

    private final UserService userService;

    @Autowired
    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/registration", headers = "Accept=application/json")
    public ResponseEntity<Object> userRegistration(@RequestBody UserRegistrationDto userRegistrationDto){
        User user;
        try {
            user = userService.registerUser(userRegistrationDto);
        } catch (ValidException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(user);
    }

    @PatchMapping(path = "/change-password")
    public ResponseEntity<Object> changePassword(@RequestBody UserChangePasswordDto user){
        if (user.getAccount().isBlank() ||
                user.getConfirmPassword().isBlank() ||
                user.getNewPassword().isBlank() ||
                user.getOldPassword().isBlank()){

            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("One or more field is empty.");
        }

        try {
            return ResponseEntity.ok(userService.changePassword(user));
        } catch (ExistInDatabaseException | ValidException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping(path = "/current-role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> searchCurrentUserRole(@RequestParam("email") String email){
        if (email.isBlank()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("Email field is empty.");
        }

        try {
            return ResponseEntity
                    .ok(userService.findCurrentRole(email));
        } catch (ExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
    @PatchMapping(path = "/change-role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> changeUserRole(@RequestBody UserChangeRoleDto userChangeRoleDto){
        if (userChangeRoleDto.getEmail().isBlank()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("Email field is empty.");
        }

        try {
            userService.changeUserRole(userChangeRoleDto);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Successfully changed user role.");
        } catch (ExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    //TODO Block account test
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @PatchMapping("/block-account/{id}")
    public ResponseEntity<?> blockUserAccount(@PathVariable("id") Long id){
        try {
            userService.blockUserAccount(id);
        } catch (ExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());

        } catch (PermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body("Account has been blocked");
    }

}
