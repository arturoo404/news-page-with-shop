package com.arturoo404.NewsPage.entity.news.comments;

import com.arturoo404.NewsPage.entity.news.article.Article;
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
            length = 1000,
            nullable = false
    )
    private String content;

    @Column(name = "comments_date")
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
