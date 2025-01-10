package com.tamerm.blog_app.integration.service.impl;

import com.tamerm.blog_app.BlogApplication;
import com.tamerm.blog_app.model.Tag;
import com.tamerm.blog_app.service.TagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the TagService.
 */
@SpringBootTest(classes = BlogApplication.class)
@Transactional
public class TagServiceIntegrationTest {

    @Autowired
    private TagService tagService;

    /**
     * Test retrieving or creating tags and expect existing tags to be returned.
     */
    @Test
    void getOrCreateTags_ShouldReturnExistingTags() {
        Set<Tag> tags = tagService.getOrCreateTags(List.of("tag1"));
        assertNotNull(tags);
        assertEquals(1, tags.size());
        assertTrue(tags.stream().anyMatch(tag -> "tag1".equals(tag.getName())));
    }

    /**
     * Test retrieving or creating tags and expect new tags to be created.
     */
    @Test
    void getOrCreateTags_ShouldCreateNewTags() {
        Set<Tag> tags = tagService.getOrCreateTags(List.of("newTag"));
        assertNotNull(tags);
        assertEquals(1, tags.size());
        assertTrue(tags.stream().anyMatch(tag -> "newTag".equals(tag.getName())));
    }

    /**
     * Test retrieving or creating multiple tags and expect all tags to be returned.
     */
    @Test
    void getOrCreateTags_ShouldHandleMultipleTags() {
        Set<Tag> tags = tagService.getOrCreateTags(List.of("tag1", "tag2"));
        assertNotNull(tags);
        assertEquals(2, tags.size());
        assertTrue(tags.stream().anyMatch(tag -> "tag1".equals(tag.getName())));
        assertTrue(tags.stream().anyMatch(tag -> "tag2".equals(tag.getName())));
    }

    /**
     * Test retrieving or creating tags and expect empty tags to be skipped.
     */
    @Test
    void getOrCreateTags_ShouldSkipEmptyTags() {
        Set<Tag> tags = tagService.getOrCreateTags(List.of("tag1", "", " "));
        assertNotNull(tags);
        assertEquals(1, tags.size());
        assertTrue(tags.stream().anyMatch(tag -> "tag1".equals(tag.getName())));
    }

    /**
     * Test retrieving or creating a large number of tags and expect all tags to be returned.
     */
    @Test
    void getOrCreateTags_ShouldHandleLargeNumberOfTags() {
        List<String> tagNames = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            tagNames.add("tag" + i);
        }
        Set<Tag> tags = tagService.getOrCreateTags(tagNames);
        assertNotNull(tags);
        assertEquals(1000, tags.size());
    }
}