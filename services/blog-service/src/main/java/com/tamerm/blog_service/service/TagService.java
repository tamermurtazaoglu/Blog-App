package com.tamerm.blog_service.service;

import com.tamerm.blog_service.model.Tag;

import java.util.List;
import java.util.Set;

public interface TagService {
    Set<Tag> getOrCreateTags(List<String> tagNames);
}
