package com.arturoo404.NewsPage.entity.user_objects.user;

import com.arturoo404.NewsPage.entity.news.comments.Comments;
import com.arturoo404.NewsPage.entity.shop.address.Address;
import com.arturoo404.NewsPage.entity.shop.cart.Cart;
import com.arturoo404.NewsPage.entity.shop.order.Order;
import com.arturoo404.NewsPage.entity.user_objects.confirm_account.ConfirmAccount;
import com.arturoo404.NewsPage.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            length = 80
    )
    private String password;

    @Column(
            unique = true,
            nullable = false,
            length = 80
    )
    private String email;

    @Column(
            nullable = false,
            length = 30
    )
    private String nick;

    @Enumerated(value = EnumType.STRING)
    private UserRole userRole;

    private boolean enabled = true;

    private boolean locked = false;

    @JsonIgnore
    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST}
    )
    private List<Comments> comments;

    @JsonIgnore
    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "cart",
            referencedColumnName = "id"
    )
    private Cart cart;


    @JsonIgnore
    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "address",
            referencedColumnName = "id"
    )
    private Address address;

    @JsonIgnore
    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "confirm_account",
            referencedColumnName = "id"
    )
    private ConfirmAccount confirmAccount;

    @JsonIgnore
    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST}
    )
    private List<Order> orders;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(userRole.name());
        return Collections.singletonList(simpleGrantedAuthority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
