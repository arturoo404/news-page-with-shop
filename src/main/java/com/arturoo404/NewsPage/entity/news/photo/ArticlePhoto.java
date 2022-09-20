package com.arturoo404.NewsPage.entity.news.photo;

import com.arturoo404.NewsPage.entity.news.content.Content;
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

    @OneToOne(mappedBy = "articlePhoto")
    private Content content;

    @Column(name = "photo_position")
    private Integer photoPosition;

    @Column(name = "photo_width")
    private Integer photoWidth;

    @Column(name = "photo_height")
    private Integer photoHeight;

    @Column(
            name = "photo_place",
            length = 10
    )
    private String photoPlace;
    public ArticlePhoto(Integer photoPosition) {
        this.photoPosition = photoPosition;
    }
}
