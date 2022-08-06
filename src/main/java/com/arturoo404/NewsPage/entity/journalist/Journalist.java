package com.arturoo404.NewsPage.entity.journalist;

import com.arturoo404.NewsPage.entity.article.Article;
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
@Table(name = "journalist")
public class Journalist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(
            name = "name",
            length = 80,
            unique = true
    )
    private String name;

    @Column(
            name = "info",
            length = 2500
    )
    private String info;

    @Column(
            name = "photo",
            length = 729496729
    )
    private byte[] photo;

    @OneToMany(
            mappedBy = "journalist",
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Article> article;
}
