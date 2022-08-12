package com.arturoo404.NewsPage.service;

import com.arturoo404.NewsPage.entity.article.Article;
import com.arturoo404.NewsPage.entity.article.dto.ArticlePageDataDto;
import com.arturoo404.NewsPage.entity.content.dto.ArticleContentDto;
import com.arturoo404.NewsPage.entity.article.dto.CreateArticleDto;
import com.arturoo404.NewsPage.entity.article.dto.TileArticleDto;
import com.arturoo404.NewsPage.entity.photo.ArticlePhoto;
import com.arturoo404.NewsPage.entity.photo.dto.ArticlePhotoAddDto;
import com.arturoo404.NewsPage.entity.photo.dto.PhotoDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ArticleService {
    Article addArticle(CreateArticleDto createArticleDto);

    void addArticlePhoto(MultipartFile photo, Long id) throws IOException;

    Integer getNumberOfPhotos(Long articleId);

    ArticlePhoto saveArticleStatistic(ArticlePhotoAddDto addDto);

    void savePhotoInsideArticle(MultipartFile photo, Long id, Integer position) throws IOException;

    Page<TileArticleDto> getArticleTile(Integer page, String tag);

    PhotoDto getMainArticlePhoto(Long id) throws FileNotFoundException;

    ArticlePageDataDto getContent(Long id);
}
