package com.arturoo404.NewsPage.repository.user;

import com.arturoo404.NewsPage.entity.user_objects.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByNick(String nick);

    User findUserByEmail(String email);
}
