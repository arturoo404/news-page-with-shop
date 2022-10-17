package com.arturoo404.NewsPage.service.impl.user;

import com.arturoo404.NewsPage.entity.shop.cart.Cart;
import com.arturoo404.NewsPage.entity.user_objects.user.User;
import com.arturoo404.NewsPage.entity.user_objects.user.dto.UserChangePasswordDto;
import com.arturoo404.NewsPage.entity.user_objects.user.dto.UserChangeRoleDto;
import com.arturoo404.NewsPage.entity.user_objects.user.dto.UserRegistrationDto;
import com.arturoo404.NewsPage.enums.UserRole;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.exception.PermissionException;
import com.arturoo404.NewsPage.exception.ValidException;
import com.arturoo404.NewsPage.repository.user.UserRepository;
import com.arturoo404.NewsPage.service.user.UserService;
import com.arturoo404.NewsPage.validation.PasswordValid;
import com.arturoo404.NewsPage.validation.RegistrationValid;
import com.arturoo404.NewsPage.validation.impl.PasswordValidImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RegistrationValid registrationValid;

    public UserServiceImpl(UserRepository userRepository, RegistrationValid registrationValid) {
        this.userRepository = userRepository;
        this.registrationValid = registrationValid;
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("We can not find this email")
        );
    }

    @Override
    public User registerUser(UserRegistrationDto userDto) throws ValidException {
        registrationValid.registrationValid(userDto);
        return userRepository.save(User.builder()
                        .email(userDto.getEmail())
                        .nick(userDto.getNick())
                        .userRole(UserRole.USER)
                        .cart(Cart.builder()
                                .amount(0D)
                                .productQuantity(0L)
                                .build())
                        .password(
                                new BCryptPasswordEncoder()
                                        .encode(userDto.getPassword())
                        )
                        .build()
                );
    }

    @Override
    public User changePassword(UserChangePasswordDto user) throws ExistInDatabaseException, ValidException {
        PasswordValid passwordValid = new PasswordValidImpl();
        passwordValid.password(user.getNewPassword(), user.getConfirmPassword());

        final Optional<User> byEmail = userRepository.findByEmail(user.getAccount());
        userPresent(byEmail);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(user.getOldPassword(), byEmail.get().getPassword())){
            throw new ValidException("Bad old password.");
        }
        byEmail.get().setPassword(encoder.encode(user.getNewPassword()));

        return userRepository.save(byEmail.get());
    }

    @Override
    public void changeUserRole(UserChangeRoleDto userRole) throws ExistInDatabaseException {
        User user = userInDataBase(userRole.getEmail());

        if (userRole.getRole().equals(user.getUserRole())){
            throw new ExistInDatabaseException("User already has this role.");
        }
        user.setUserRole(userRole.getRole());
        userRepository.save(user);
    }

    @Override
    public Object findCurrentRole(String email) throws ExistInDatabaseException {
        final User user = userInDataBase(email);

        return new UserChangeRoleDto(user.getEmail(), user.getUserRole());
    }

    @Override
    public void blockUserAccount(Long id) throws ExistInDatabaseException, PermissionException {
        final Optional<User> byId = userRepository.findById(id);
        userPresent(byId);
        User user = byId.get();

        if (!user.getUserRole().equals(UserRole.USER)){
            throw new PermissionException("You do not have permission to block this user.");
        }
        user.setLocked(true);

        userRepository.save(user);
    }

    private void userPresent(Optional<User> byId) throws ExistInDatabaseException {
        if (byId.isEmpty()){
            throw new ExistInDatabaseException("User not found.");
        }
    }


    private User userInDataBase(String email) throws ExistInDatabaseException {
        final Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isEmpty()) {
            throw new ExistInDatabaseException("User not found.");
        }
        return byEmail.get();
    }
}
