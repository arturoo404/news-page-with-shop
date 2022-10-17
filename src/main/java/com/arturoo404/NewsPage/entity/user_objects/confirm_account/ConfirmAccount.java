package com.arturoo404.NewsPage.entity.user_objects.confirm_account;

import com.arturoo404.NewsPage.entity.user_objects.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "confirm_account")
public class ConfirmAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "confirm_link",
            length = 100
    )
    private String confirmLink;

    @Column(
            name = "link_status"
    )
    private Boolean linkStatus;

    @OneToOne(mappedBy = "confirmAccount")
    private User user;
}
