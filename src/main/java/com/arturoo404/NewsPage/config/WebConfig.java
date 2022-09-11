package com.arturoo404.NewsPage.config;

import com.arturoo404.NewsPage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebConfig {

    @Autowired
    private UserService userService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/registration").permitAll()
                .antMatchers("/api/user/registration").permitAll()
                .antMatchers("/api/article/tile").permitAll()
                .antMatchers("/api/article/{\\\\d+}/photo").permitAll()
                .antMatchers("/api/article/content").permitAll()
                .antMatchers("/api/weather/**").permitAll()
                .antMatchers("/api/geolocation/**").permitAll()
                .antMatchers("/api/article/search").permitAll()
                .antMatchers("/api/article/title").permitAll()
                .antMatchers("/api/article/photo/inside/{\\\\d+}").permitAll()
                .antMatchers("/api/article/last-published/**").permitAll()
                .antMatchers("/api/article/popularity").permitAll()
                .antMatchers("/article").permitAll()
                .antMatchers("/article/search").permitAll()
                .antMatchers("/article/detail").permitAll()
                .antMatchers("/weather").permitAll()
                .antMatchers("/api/comments/list").permitAll()
                //TEST PLACE
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/", true)
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .rememberMe()
                .userDetailsService(this.userService)
                .key("remember-me");

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
