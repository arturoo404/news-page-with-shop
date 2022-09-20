package com.arturoo404.NewsPage.entity.news.article.dto;

import com.arturoo404.NewsPage.entity.news.content.dto.ArticleContentDto;
import com.arturoo404.NewsPage.entity.news.journalist.dto.JournalistGetDto;
import com.arturoo404.NewsPage.entity.news.photo.dto.PhotoPropertiesDto;
import com.arturoo404.NewsPage.enums.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticlePageDataDto {

    private String title;
    private JournalistGetDto journalistGetDto;
    private List<Tag> tags;
    private List<ArticleContentDto> contentDto;
    private List<PhotoPropertiesDto> articlePhotoPropertiesDto;
}
