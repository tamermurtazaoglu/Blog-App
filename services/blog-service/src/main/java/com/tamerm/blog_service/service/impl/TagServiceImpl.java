package com.tamerm.blog_service.service.impl;

import com.tamerm.blog_service.model.Tag;
import com.tamerm.blog_service.repository.TagRepository;
import com.tamerm.blog_service.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Transactional
    @Override
    public Set<Tag> getOrCreateTags(List<String> tagNames) {
        return tagNames.stream()
                .filter(name -> name != null && !name.trim().isEmpty())
                .map(name -> tagRepository.findByName(name)
                        .orElseGet(() -> tagRepository.save(new Tag(null, name))))
                .collect(Collectors.toSet());
    }
}
