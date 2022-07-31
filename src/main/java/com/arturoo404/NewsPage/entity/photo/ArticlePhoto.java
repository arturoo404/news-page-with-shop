package com.arturoo404.NewsPage.entity.photo;

import com.arturoo404.NewsPage.entity.content.Content;
import lombok.*;

import javax.persistence.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "article_photo")
public class ArticlePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "content_photo",
            length = 729496729
    )
    private byte[] contentPhoto;

    @ManyToOne()
    @JoinColumn(
            name = "content",
            nullable = false
    )
    private Content content;
}
