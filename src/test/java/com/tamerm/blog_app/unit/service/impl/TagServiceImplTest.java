package com.tamerm.blog_app.unit.service.impl;

import com.tamerm.blog_app.model.Tag;
import com.tamerm.blog_app.repository.TagRepository;
import com.tamerm.blog_app.service.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link TagServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    private Tag tag;

    @BeforeEach
    void setUp() {
        tag = new Tag("tag1");
    }

    /**
     * Tests that existing tags are returned correctly.
     */
    @Test
    void getOrCreateTags_ShouldReturnExistingTags() {
        when(tagRepository.findByName(anyString())).thenReturn(Optional.of(tag));

        Set<Tag> result = tagService.getOrCreateTags(List.of("tag1"));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(tag));
        verify(tagRepository, times(1)).findByName("tag1");
    }

    /**
     * Tests that new tags are created correctly.
     */
    @Test
    void getOrCreateTags_ShouldCreateNewTags() {
        when(tagRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        Set<Tag> result = tagService.getOrCreateTags(List.of("tag1"));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(tag));
        verify(tagRepository, times(1)).findByName("tag1");
        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    /**
     * Tests that multiple tags are handled correctly.
     */
    @Test
    void getOrCreateTags_ShouldHandleMultipleTags() {
        Tag tag2 = new Tag("tag2");
        when(tagRepository.findByName("tag1")).thenReturn(Optional.of(tag));
        when(tagRepository.findByName("tag2")).thenReturn(Optional.of(tag2));

        Set<Tag> result = tagService.getOrCreateTags(List.of("tag1", "tag2"));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(tag));
        assertTrue(result.contains(tag2));
        verify(tagRepository, times(1)).findByName("tag1");
        verify(tagRepository, times(1)).findByName("tag2");
    }

    /**
     * Tests that empty and whitespace-only tags are skipped.
     */
    @Test
    void getOrCreateTags_ShouldSkipEmptyTags() {
        when(tagRepository.findByName("tag1")).thenReturn(Optional.of(tag));

        Set<Tag> result = tagService.getOrCreateTags(List.of("tag1", "", " "));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(tag));
        verify(tagRepository, times(1)).findByName("tag1");
        verify(tagRepository, never()).findByName("");
        verify(tagRepository, never()).findByName(" ");
    }
}