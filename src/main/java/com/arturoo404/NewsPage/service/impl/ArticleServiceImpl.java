package com.arturoo404.NewsPage.service.impl;

import com.arturoo404.NewsPage.converter.TagConverter;
import com.arturoo404.NewsPage.entity.article.Article;
import com.arturoo404.NewsPage.entity.article.dto.ArticlePageContentDto;
import com.arturoo404.NewsPage.entity.article.dto.CreateArticleDto;
import com.arturoo404.NewsPage.entity.article.dto.TileArticleDto;
import com.arturoo404.NewsPage.entity.content.Content;
import com.arturoo404.NewsPage.entity.photo.ArticlePhoto;
import com.arturoo404.NewsPage.entity.photo.dto.ArticlePhotoAddDto;
import com.arturoo404.NewsPage.entity.photo.dto.PhotoDto;
import com.arturoo404.NewsPage.entity.tag.Tags;
import com.arturoo404.NewsPage.enums.ContentType;
import com.arturoo404.NewsPage.enums.Tag;
import com.arturoo404.NewsPage.repository.ArticlePhotoRepository;
import com.arturoo404.NewsPage.repository.ArticleRepository;
import com.arturoo404.NewsPage.repository.JournalistRepository;
import com.arturoo404.NewsPage.repository.TagRepository;
import com.arturoo404.NewsPage.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    private final ArticlePhotoRepository articlePhotoRepository;

    private final JournalistRepository journalistRepository;

    private final TagRepository tagRepository;

    private final TagConverter tagConverter;

    @Autowired
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
        articlePhoto.setPhotoHeight(addDto.getPhotoHeight());
        articlePhoto.setPhotoWidth(addDto.getPhotoWidth());
        articlePhoto.setPhotoPlace(addDto.getPhotoPlace());
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
    public List<ArticlePageContentDto> getContent(Long id) {
        return articleRepository.findArticleById(id)
                .getContent().stream()
                .map(a -> new ArticlePageContentDto(a.getId(), a.getContentType(), a.getText()))
                .collect(Collectors.toList());
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
