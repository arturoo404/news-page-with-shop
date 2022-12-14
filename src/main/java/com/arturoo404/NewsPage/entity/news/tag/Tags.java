package com.arturoo404.NewsPage.entity.news.tag;

import com.arturoo404.NewsPage.entity.news.article.Article;
import com.arturoo404.NewsPage.enums.Tag;
import lombok.*;

import javax.persistence.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tags")
public class Tags {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "tag")
    private Tag tag;

    @ManyToOne()
    @JoinColumn(
            name = "article_tag",
            nullable = false
    )
    private Article articleTag;

    public Tags(Tag tag, Article articleTag) {
        this.tag = tag;
        this.articleTag = articleTag;
    }
}
