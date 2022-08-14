package com.arturoo404.NewsPage.entity.comments;

import com.arturoo404.NewsPage.entity.article.Article;
import com.arturoo404.NewsPage.entity.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "content",
            length = 500,
            nullable = false
    )
    private String content;

    @Column(name = "date")
    private Date date;

    @ManyToOne
    @JoinColumn(
            name = "article"
    )
    private Article article;

    @ManyToOne
    @JoinColumn(
            name = "user"
    )
    private User user;
}
