package com.arturoo404.NewsPage.service.impl;

import com.arturoo404.NewsPage.converter.TagConverter;
import com.arturoo404.NewsPage.entity.article.Article;
import com.arturoo404.NewsPage.entity.article.dto.*;
import com.arturoo404.NewsPage.entity.content.dto.ArticleContentDto;
import com.arturoo404.NewsPage.entity.content.Content;
import com.arturoo404.NewsPage.entity.journalist.dto.JournalistGetDto;
import com.arturoo404.NewsPage.entity.photo.ArticlePhoto;
import com.arturoo404.NewsPage.entity.photo.dto.ArticlePhotoAddDto;
import com.arturoo404.NewsPage.entity.photo.dto.PhotoDto;
import com.arturoo404.NewsPage.entity.photo.dto.PhotoPropertiesDto;
import com.arturoo404.NewsPage.entity.tag.Tags;
import com.arturoo404.NewsPage.enums.ContentType;
import com.arturoo404.NewsPage.enums.Tag;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.repository.ArticlePhotoRepository;
import com.arturoo404.NewsPage.repository.ArticleRepository;
import com.arturoo404.NewsPage.repository.JournalistRepository;
import com.arturoo404.NewsPage.repository.TagRepository;
import com.arturoo404.NewsPage.service.ArticleService;
import org.hibernate.annotations.BatchSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private final ArticleRepository articleRepository;
    @Autowired
    private final ArticlePhotoRepository articlePhotoRepository;

    @Autowired
    private final JournalistRepository journalistRepository;
    @Autowired
    private final TagRepository tagRepository;
    @Autowired
    private final TagConverter tagConverter;

    public ArticleServiceImpl(ArticleRepository articleRepository, ArticlePhotoRepository articlePhotoRepository, JournalistRepository journalistRepository, TagRepository tagRepository, TagConverter tagConverter) {
        this.articleRepository = articleRepository;
        this.articlePhotoRepository = articlePhotoRepository;
        this.journalistRepository = journalistRepository;
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
    }

    @Override
    public Article addArticle(CreateArticleDto createArticleDto) {
        Article article = Article.builder()
                .title(createArticleDto.getTitle())
                .articleStatus(false)
                .journalist(journalistRepository.findJournalistById(createArticleDto.getJournalist()))
                .articlePopularity(0L)
                .build();

        article.setTags(
                tagConverter.getTagList(createArticleDto.getTags()).stream()
                        .map(t -> new Tags(t, article))
                        .collect(Collectors.toList())
        );
        article.setContent(contentList(createArticleDto.getContent(), article));

        return articleRepository.save(article);
    }

    @Override
    public void addArticlePhoto(MultipartFile photo, Long id) throws IOException {
        Article article = articleRepository.findArticleById(id);
        article.setArticleMainPhoto(photo.getBytes());
        articleRepository.save(article);
    }

    @Override
    public Integer getNumberOfPhotos(Long articleId) {
        Article article = articleRepository.findArticleById(articleId);
        return Math.toIntExact(article.getContent().stream()
                .filter(a -> a.getContentType().equals(ContentType.PHOTO))
                .count());
    }

    @Override
    public ArticlePhoto saveArticleStatistic(ArticlePhotoAddDto addDto) {
        ArticlePhoto articlePhoto = articlePhotoRepository
                .findByArticleIdAndPosition(addDto.getArticleId(), addDto.getPhotoPosition());

        if (addDto.getPhotoPlace().isBlank() || addDto.getPhotoHeight() < 0 || addDto.getPhotoWidth() < 0){
            articlePhoto.setPhotoHeight(600);
            articlePhoto.setPhotoWidth(800);
            articlePhoto.setPhotoPlace("left");
            return articlePhotoRepository.save(articlePhoto);
        }

        String photoPlace = addDto.getPhotoPlace().toLowerCase();

        if (photoPlace.equals("left") || photoPlace.equals("center") || photoPlace.equals("right")){
            articlePhoto.setPhotoHeight(addDto.getPhotoHeight());
            articlePhoto.setPhotoWidth(addDto.getPhotoWidth());
            articlePhoto.setPhotoPlace(photoPlace);
            return articlePhotoRepository.save(articlePhoto);
        }

        articlePhoto.setPhotoHeight(addDto.getPhotoHeight());
        articlePhoto.setPhotoWidth(addDto.getPhotoWidth());
        articlePhoto.setPhotoPlace("left");
        return articlePhotoRepository.save(articlePhoto);
    }

    @Override
    public void savePhotoInsideArticle(MultipartFile photo, Long id, Integer contentId) throws IOException {
        ArticlePhoto articlePhoto = articlePhotoRepository
                .findByArticleIdAndPosition(id, contentId);
        articlePhoto.setContentPhoto(photo.getBytes());
        articlePhotoRepository.save(articlePhoto);
    }

    @Override
    public Page<TileArticleDto> getArticleTile(Integer page, String tag) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("id").descending());
        Tag tagConv = tagConverter.getSingleTag(tag);
        return tagRepository.findAllByTag(tagConv, pageable)
                .map(t -> new TileArticleDto(t.getArticleTag().getId(), t.getArticleTag().getTitle()));
    }

    @Override
    public PhotoDto getMainArticlePhoto(Long id) throws FileNotFoundException {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isEmpty()){
            throw new FileNotFoundException("Photo not found.");
        }
        return new PhotoDto(article.get().getArticleMainPhoto());
    }
    @Override
    public ArticlePageDataDto getContent(Long id) throws ExistInDatabaseException {
        Article article = articleRepository.findArticleById(id);

        if (article == null){
            throw new ExistInDatabaseException("Article not found");
        }

        return ArticlePageDataDto.builder()
                .title(article.getTitle())
                .journalistGetDto(new JournalistGetDto(
                        article.getJournalist().getId(),
                        article.getJournalist().getName()
                ))
                .tags(article.getTags().stream()
                        .map(Tags::getTag)
                        .collect(Collectors.toList()))
                .contentDto(article.getContent().stream()
                .map(a -> new ArticleContentDto(
                        a.getId(),
                        a.getContentType(),
                        a.getText())
                )
                .collect(Collectors.toList()))
                .articlePhotoPropertiesDto(article.getContent().stream()
                        .filter(a -> a.getContentType().equals(ContentType.PHOTO))
                        .map(a -> PhotoPropertiesDto.builder()
                                .photoId(a.getArticlePhoto().getId())
                                .photoPosition(a.getArticlePhoto().getPhotoPosition())
                                .photoWidth(a.getArticlePhoto().getPhotoWidth())
                                .photoHeight(a.getArticlePhoto().getPhotoHeight())
                                .photoPlace(a.getArticlePhoto().getPhotoPlace())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public ArticleTitleDto getTitle(Long id) {
        ArticleTitleDto articleTitleDto = new ArticleTitleDto();
        articleTitleDto.setTitle(articleRepository.findArticleById(id).getTitle());
        return articleTitleDto;
    }

    @Override
    public PhotoDto getArticleInsidePhoto(Long id) throws FileNotFoundException {
        final Optional<ArticlePhoto> articlePhoto = articlePhotoRepository.findById(id);
        if (articlePhoto.isEmpty()){
            throw new FileNotFoundException("Photo not found");
        }
        return new PhotoDto(articlePhoto.get().getContentPhoto());
    }

    @Override
    public Page<ArticleStatusListDto> getArticleList(Integer page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("id").descending());
        return articleRepository.findAll(pageable)
                .map(a -> ArticleStatusListDto.builder()
                        .articleId(a.getId())
                        .status(a.isArticleStatus())
                        .journalist(a.getJournalist().getName())
                        .title(a.getTitle())
                        .build());
    }

    @Override
    public void changeArticleStatus(Long articleId) {
        Article article = articleRepository.findArticleById(articleId);
        article.setArticleStatus(!article.isArticleStatus());
        articleRepository.save(article);
    }

    @Override
    public void deleteArticle(Long id) {
        articleRepository.delete(articleRepository.findArticleById(id));
    }

    @Override
    public Page<TileArticleDto> getArticleTileByKeyword(Integer page, String keyword) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("id").descending());
        return articleRepository.findAllByKeyword(keyword, pageable)
                .map(a -> new TileArticleDto(a.getId(), a.getTitle()));
    }

    @Override
    public void changeArticlePopularity(Long id) {
        Article article = articleRepository.findArticleById(id);
        articleRepository.updatePopularityRanking(id, article.getArticlePopularity() + 1);
    }

    @Override
    public List<TileArticleDto> getLastPublishedArticleList() {
        return articleRepository.findArticleLastPublishedList()
                .stream()
                .map(a -> new TileArticleDto(a.getId(), a.getTitle()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TileArticleDto> lastPublishedArticleByTagList(String tag) {
        final Tag singleTag = tagConverter.getSingleTag(tag);
        Pageable pageable = PageRequest.of(0, 5);
        final Page<Tags> articlePage
                = tagRepository.findArticleLastPublishedListByTag(pageable, singleTag);

        return articlePage.stream()
                .map(p -> new TileArticleDto(
                        p.getArticleTag().getId(),
                        p.getArticleTag().getTitle())
                ).collect(Collectors.toList());
    }

    @Override
    public List<TileArticleDto> getPopularityArticle() {
        Pageable pageable = PageRequest.of(0, 5);
        return articleRepository.findArticleByPopularity(pageable)
                .stream()
                .map(p -> new TileArticleDto(
                        p.getId(),
                        p.getTitle())
                ).collect(Collectors.toList());
    }

    private List<Content> contentList(String content, Article article){

        String[] contentTextSplit = content.split("@text");

        List<Content> contents = new ArrayList<>();
        int photoPos = 1;

        for (String cont : contentTextSplit){
            String[] contentTitleSplit = cont.split("@title");
            String[] contentPhotoSplit = cont.split("@photo");
            String[] contentArticleSplit = cont.split("@article");

            if (contentTitleSplit.length == 1 && contentPhotoSplit.length == 1 && contentArticleSplit.length == 1){
                contents.add(new Content(ContentType.TEXT, cont, article));
            }

            if (contentTitleSplit.length == 2){
                contents.add(new Content(ContentType.TEXT, contentTitleSplit[0], article));
                contents.add(new Content(ContentType.SUBTITLE, contentTitleSplit[1], article));
            }

            if (contentPhotoSplit.length == 2){
                contents.add(new Content(ContentType.PHOTO, new ArticlePhoto(photoPos), article));
                photoPos++;
            }

            if (contentArticleSplit.length == 2){
                contents.add(new Content(ContentType.ARTICLE, contentArticleSplit[1], article));
            }

        }
        return  contents;
    }

}
