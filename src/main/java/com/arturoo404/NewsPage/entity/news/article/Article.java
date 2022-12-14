package com.arturoo404.NewsPage.entity.news.article;

import com.arturoo404.NewsPage.entity.news.comments.Comments;
import com.arturoo404.NewsPage.entity.news.content.Content;
import com.arturoo404.NewsPage.entity.news.journalist.Journalist;
import com.arturoo404.NewsPage.entity.news.tag.Tags;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "journnalist"
    )
    private Journalist journalist;

    @Column(
            name = "title",
            length = 100
    )
    private String title;

    @OneToMany(
            mappedBy = "article",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Content> content;

    @OneToMany(
            mappedBy = "articleTag",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Tags> tags;

    @Column(name = "article_popularity")
    private Long articlePopularity;

    @Column(
            name = "article_main_photo",
            length = 729496729
    )
    private byte[] articleMainPhoto;

    @Column(name = "article_status")
    private boolean articleStatus;

    @OneToMany(
            mappedBy = "article",
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Comments> comments;
}
