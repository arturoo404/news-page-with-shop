package com.arturoo404.NewsPage.converter.impl;

import com.arturoo404.NewsPage.converter.TagConverter;
import com.arturoo404.NewsPage.enums.Tag;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagConverterImpl implements TagConverter {

    @Override
    public List<Tag> getTagList(List<String> tag) {
        return tag.stream()
                .map(this::tag)
                .collect(Collectors.toList());
    }

    @Override
    public Tag getSingleTag(String tag) {
        return tag(tag);
    }

    private Tag tag(String name){

        switch (name){
            case "world":
                return Tag.WORLD;
            case "sports":
                return Tag.SPORTS;
            case "tech":
                return Tag.TECH;
            case "politics":
                return Tag.POLITICS;
            case "news":
                return Tag.NEWS;
            case "science":
                return Tag.SCIENCE;
            case "country":
                return Tag.COUNTRY;
        }

        return Tag.NEWS;
    }
}
