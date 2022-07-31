package com.arturoo404.NewsPage.entity.tag;

import com.arturoo404.NewsPage.entity.article.Article;
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
}
