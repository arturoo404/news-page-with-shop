package com.arturoo404.NewsPage.entity.news.content;

import com.arturoo404.NewsPage.entity.news.article.Article;
import com.arturoo404.NewsPage.entity.news.photo.ArticlePhoto;
import com.arturoo404.NewsPage.enums.ContentType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "content")
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "content_type")
    private ContentType contentType;

    @Column(
            name = "text",
            length = 10000
    )
    private String text;

    @OneToOne(
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "article_photo",
            referencedColumnName = "id"
    )
    @JsonIgnore
    private ArticlePhoto articlePhoto;

    @ManyToOne()
    @JoinColumn(
            name = "article"
    )
    private Article article;

    public Content(ContentType contentType, String text, Article article) {
        this.contentType = contentType;
        this.text = text;
        this.article = article;
    }

    public Content(ContentType contentType, ArticlePhoto articlePhoto, Article article) {
        this.contentType = contentType;
        this.articlePhoto = articlePhoto;
        this.article = article;
    }
}
