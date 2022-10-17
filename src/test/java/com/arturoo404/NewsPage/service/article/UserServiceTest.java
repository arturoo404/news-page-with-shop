package com.arturoo404.NewsPage.service.article;

import com.arturoo404.NewsPage.entity.user_objects.user.User;
import com.arturoo404.NewsPage.entity.user_objects.user.dto.UserChangePasswordDto;
import com.arturoo404.NewsPage.entity.user_objects.user.dto.UserChangeRoleDto;
import com.arturoo404.NewsPage.entity.user_objects.user.dto.UserRegistrationDto;
import com.arturoo404.NewsPage.enums.UserRole;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.exception.ValidException;
import com.arturoo404.NewsPage.microservice.EmailSenderService;
import com.arturoo404.NewsPage.repository.user.UserRepository;
import com.arturoo404.NewsPage.service.user.UserService;
import com.arturoo404.NewsPage.service.impl.user.UserServiceImpl;
import com.arturoo404.NewsPage.validation.RegistrationValid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.mail.MessagingException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RegistrationValid valid;

    @Mock
    private EmailSenderService emailSenderService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, valid, emailSenderService);
    }

    @Test
    void itShouldLoadUserByUsername(){
        //Given
        String email = "test@gmail.com";

        User user = User.builder()
                .userRole(UserRole.USER)
                .nick("account")
                .password("password")
                .enabled(true)
                .locked(false)
                .email(email)
                .build();

        //When
        when(userRepository.findByEmail(email)).thenReturn(Optional.ofNullable(user));
        final UserDetails userDetails = userService.loadUserByUsername(email);

        //Then
        assertThat(userDetails.getUsername()).isEqualTo(email);
    }

    @Test
    void registerUserBadEmail() {
        //Given
        User user = new User();

        UserRegistrationDto userDto = UserRegistrationDto.builder()
                .nick("account")
                .password("password")
                .passwordConfirm("pasword")
                .email("emailgmail.com")
                .build();
        //When
        when(userRepository.save(any(User.class))).thenReturn(User.builder()
                        .userRole(UserRole.USER)
                        .nick(userDto.getNick())
                        .id(1L)
                        .email(userDto.getEmail())
                        .password(userDto.getPassword())
                .build());
        try {
           user = userService.registerUser(userDto);
        } catch (ValidException | MessagingException e) {
            assertThat(e.getMessage()).isNotBlank();
        }

        //Then
        assertThat(user.getUserRole()).isEqualTo(UserRole.USER);
        assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
    }

    @Test
    void itShouldExistInDatabaseException(){
        //Given
        UserChangePasswordDto userDto =
                new UserChangePasswordDto("AFafVV#%3535ccaD", "AFafVV#%3535ccaD", "pass", "acc");

        //When
        try {
            userService.changePassword(userDto);
        } catch (ExistInDatabaseException | ValidException e) {
            assertThat(e.getMessage()).isEqualTo("User not found.");
        }
        //Then
    }

    @Test
    void itShouldThrowExceptionWhenOldPasswordWasDifferenceWithDatabasePassword(){
        //Given
        UserChangePasswordDto userDto =
                new UserChangePasswordDto("AFafVV#%3535ccaD", "AFafVV#%3535ccaD", "pass", "acc");

        //When
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(User.builder()
                .userRole(UserRole.USER)
                .nick("acc")
                .id(1L)
                .email("email@gmail.com")
                .password("badpass")
                .build()));

        try {
            userService.changePassword(userDto);
        } catch (ExistInDatabaseException | ValidException e) {
            assertThat(e.getMessage()).isEqualTo("Bad old password.");
        }
        //Then
    }

    @Test
    void itShouldReturnUser() throws ExistInDatabaseException, ValidException {
        //Given
        UserChangePasswordDto userDto =
                new UserChangePasswordDto("AFafVV#%3535ccaD", "AFafVV#%3535ccaD", "goodpass", "acc");
        final String encode = new BCryptPasswordEncoder().encode(userDto.getOldPassword());
        User user;

        //When
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(User.builder()
                .userRole(UserRole.USER)
                .nick("acc")
                .id(1L)
                .email("email@gmail.com")
                .password(encode)
                .build()));

        when(userRepository.save(any(User.class))).thenReturn(User.builder()
                .userRole(UserRole.USER)
                .nick("acc")
                .id(1L)
                .email("email@gmail.com")
                .password(encode)
                .build());

        user = userService.changePassword(userDto);

        //Then
        assertThat(user).isNotNull();
        assertThat(userDto.getNewPassword()).isNotEqualTo(user.getPassword());
    }
    @Test
    void itShouldThrowExceptionWhenUserNotFoundDuringChangedRole(){
        //Given
        UserChangeRoleDto userRole = new UserChangeRoleDto("email@gmail.com", UserRole.ADMIN);

        //When
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        try {
            userService.changeUserRole(userRole);
        } catch (ExistInDatabaseException e) {
            assertThat(e.getMessage()).isEqualTo("User not found.");
        }
        //Then
    }

    @Test
    void itShouldThrowExceptionWhenUserAlreadyHasThisRole(){
        //Given
        UserChangeRoleDto userRole = new UserChangeRoleDto("email@gmail.com", UserRole.ADMIN);

        //When
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(User.builder()
                .userRole(UserRole.ADMIN)
                .nick("acc")
                .id(1L)
                .email("email@gmail.com")
                .password("pass")
                .build()));

        try {
            userService.changeUserRole(userRole);
        } catch (ExistInDatabaseException e) {
            assertThat(e.getMessage()).isEqualTo("User already has this role.");
        }
        //Then
    }

    @Test
    void itShouldReturnCurrentUserRole() throws ExistInDatabaseException {
        //Given
        String email = "email@gmail.com";

        //When
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(User.builder()
                .userRole(UserRole.ADMIN)
                .nick("acc")
                .id(1L)
                .email(email)
                .password("pass")
                .build()));

        final Object currentRole = userService.findCurrentRole(email);
        //Then

        assertThat(currentRole).isNotNull();
    }
}