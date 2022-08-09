package com.arturoo404.NewsPage.converter;

import com.arturoo404.NewsPage.enums.Tag;

import java.util.List;

public interface TagConverter {
    
    List<Tag> getTagList(List<String> tag);

    Tag getSingleTag(String tag);
}
