package com.arturoo404.NewsPage.entity.article;

import com.arturoo404.NewsPage.entity.content.Content;
import com.arturoo404.NewsPage.entity.journalist.Journalist;
import com.arturoo404.NewsPage.entity.tag.Tags;
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

    @OneToOne(
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "journalist",
            referencedColumnName = "id"
    )
    private Journalist journalist;

    @Column(
            name = "title",
            length = 200
    )
    private String title;

    @OneToMany(
            mappedBy = "article",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Content> content;

    @OneToMany(
            mappedBy = "articleTag",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Tags> tags;

    @Column(
            name = "article_photo",
            length = 729496729
    )
    private byte[] articlePhoto;

    @Column(name = "article_status")
    private boolean articleStatus;
}
