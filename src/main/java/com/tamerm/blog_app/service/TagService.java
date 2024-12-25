package com.tamerm.blog_app.service;

import com.tamerm.blog_app.model.Tag;

import java.util.List;
import java.util.Set;

public interface TagService {
    Set<Tag> getOrCreateTags(List<String> tagNames);
}
