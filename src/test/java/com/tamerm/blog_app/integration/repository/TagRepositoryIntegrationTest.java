package com.tamerm.blog_app.integration.repository;

import com.tamerm.blog_app.BlogApplication;
import com.tamerm.blog_app.model.Tag;
import com.tamerm.blog_app.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the TagRepository.
 */
@SpringBootTest(classes = BlogApplication.class)
@Transactional
public class TagRepositoryIntegrationTest {

    @Autowired
    private TagRepository tagRepository;

    /**
     * Test finding a tag by name and expect the tag to be returned.
     */
    @Test
    void findByName_ShouldReturnTag() {
        Tag tag = new Tag("tag1");
        tagRepository.save(tag);

        Optional<Tag> foundTag = tagRepository.findByName("tag1");

        assertTrue(foundTag.isPresent());
        assertEquals("tag1", foundTag.get().getName());
    }

    /**
     * Test finding a tag by name and expect an empty result when the tag is not found.
     */
    @Test
    void findByName_ShouldReturnEmpty_WhenTagNotFound() {
        Optional<Tag> foundTag = tagRepository.findByName("nonexistentTag");

        assertFalse(foundTag.isPresent());
    }
}