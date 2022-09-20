package com.arturoo404.NewsPage.service.news;

import com.arturoo404.NewsPage.entity.news.article.Article;
import com.arturoo404.NewsPage.entity.news.article.dto.*;
import com.arturoo404.NewsPage.entity.news.photo.ArticlePhoto;
import com.arturoo404.NewsPage.entity.news.photo.dto.ArticlePhotoAddDto;
import com.arturoo404.NewsPage.entity.news.photo.dto.PhotoDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
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

    ArticlePageDataDto getContent(Long id) throws ExistInDatabaseException;

    ArticleTitleDto getTitle(Long id);

    PhotoDto getArticleInsidePhoto(Long id) throws FileNotFoundException;

    Page<ArticleStatusListDto> getArticleList(Integer page);

    void changeArticleStatus(Long articleId);

    void deleteArticle(Long id);

    Page<TileArticleDto> getArticleTileByKeyword(Integer page, String keyword);

    void changeArticlePopularity(Long id);

    List<TileArticleDto> getLastPublishedArticleList();

    List<TileArticleDto> lastPublishedArticleByTagList(String tag);

    List<TileArticleDto> getPopularityArticle();
}
