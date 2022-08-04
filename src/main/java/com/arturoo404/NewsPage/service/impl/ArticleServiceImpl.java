package com.arturoo404.NewsPage.service.impl;

import com.arturoo404.NewsPage.converter.TagConverter;
import com.arturoo404.NewsPage.entity.article.Article;
import com.arturoo404.NewsPage.entity.article.dto.CreateArticleDto;
import com.arturoo404.NewsPage.entity.content.Content;
import com.arturoo404.NewsPage.entity.photo.ArticlePhoto;
import com.arturoo404.NewsPage.entity.tag.Tags;
import com.arturoo404.NewsPage.enums.ContentType;
import com.arturoo404.NewsPage.repository.ArticleRepository;
import com.arturoo404.NewsPage.repository.JournalistRepository;
import com.arturoo404.NewsPage.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    private final JournalistRepository journalistRepository;

    private final TagConverter tagConverter;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, JournalistRepository journalistRepository, TagConverter tagConverter) {
        this.articleRepository = articleRepository;
        this.journalistRepository = journalistRepository;
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

    private List<Content> contentList(String content, Article article){

        String[] contentTextSplit = content.split("@text");

        List<Content> contents = new ArrayList<>();
        int photoPos = 1;

        for (String cont : contentTextSplit){
            String[] contentTitleSplit = cont.split("@title");
            String[] contentPhotoSplit = cont.split("@photo");
            if (contentTitleSplit.length == 1 && contentPhotoSplit.length == 1){
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
        }
        return  contents;
    }
}
