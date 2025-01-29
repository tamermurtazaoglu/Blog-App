package com.tamerm.blog_app.service.impl;

import com.tamerm.blog_app.exception.BadRequestException;
import com.tamerm.blog_app.model.Tag;
import com.tamerm.blog_app.repository.TagRepository;
import com.tamerm.blog_app.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service implementation for managing tags.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    /**
     * Retrieves or creates tags based on the provided tag names.
     *
     * @param tagNames the list of tag names
     * @return a set of tags
     * @throws BadRequestException if the tag names are null or empty
     */
    @Transactional
    @Override
    public Set<Tag> getOrCreateTags(List<String> tagNames) {
        log.debug("Retrieving or creating tags for tag names: {}", tagNames);
        if (tagNames == null || tagNames.isEmpty()) {
            log.error("Tag names cannot be null or empty");
            throw new BadRequestException("Tag names cannot be null or empty");
        }

        Set<Tag> tags = tagNames.stream()
                .filter(tagName -> tagName != null && !tagName.trim().isEmpty())
                .map(tagName -> tagRepository.findByName(tagName)
                        .orElseGet(() -> {
                            log.debug("Creating new tag with name: {}", tagName);
                            return tagRepository.save(new Tag(tagName));
                        }))
                .collect(Collectors.toSet());

        log.info("Tags retrieved or created successfully: {}", tags);
        return tags;
    }
}