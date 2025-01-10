package com.tamerm.blog_app.unit.util.mapper;

import com.tamerm.blog_app.dto.PostDTO;
import com.tamerm.blog_app.dto.PostSummaryDTO;
import com.tamerm.blog_app.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for ModelMapper.
 */
class ModelMapperTest {

    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
    }

    /**
     * Tests mapping from Post to PostDTO.
     */
    @Test
    void testMapPostToPostDTO() {
        Post post = new Post(1L, "Test Title", "Test Content", new HashSet<>());

        PostDTO postDTO = modelMapper.map(post, PostDTO.class);

        assertEquals(post.getId(), postDTO.getId());
        assertEquals(post.getTitle(), postDTO.getTitle());
        assertEquals(post.getText(), postDTO.getText());
        assertEquals(post.getTags(), postDTO.getTags());
    }

    /**
     * Tests mapping from PostDTO to Post.
     */
    @Test
    void testMapPostDTOToPost() {
        PostDTO postDTO = new PostDTO(1L, "Test Title", "Test Content", new HashSet<>());

        Post post = modelMapper.map(postDTO, Post.class);

        assertEquals(postDTO.getId(), post.getId());
        assertEquals(postDTO.getTitle(), post.getTitle());
        assertEquals(postDTO.getText(), post.getText());
        assertEquals(postDTO.getTags(), post.getTags());
    }

    /**
     * Tests mapping from Post to PostSummaryDTO.
     */
    @Test
    void testMapPostToPostSummaryDTO() {
        Post post = new Post(1L, "Test Title", "Test Content", new HashSet<>());

        PostSummaryDTO postSummaryDTO = modelMapper.map(post, PostSummaryDTO.class);

        assertEquals(post.getTitle(), postSummaryDTO.getTitle());
        assertEquals(post.getText(), postSummaryDTO.getText());
    }

    /**
     * Tests mapping from PostSummaryDTO to Post.
     */
    @Test
    void testMapPostSummaryDTOToPost() {
        PostSummaryDTO postSummaryDTO = new PostSummaryDTO("Test Title", "Test Content");

        Post post = modelMapper.map(postSummaryDTO, Post.class);

        assertEquals(postSummaryDTO.getTitle(), post.getTitle());
        assertEquals(postSummaryDTO.getText(), post.getText());
    }

    /**
     * Tests mapping with null values.
     */
    @Test
    void testMapPostWithNullValues() {
        Post post = new Post(1L, null, null, null);

        PostDTO postDTO = modelMapper.map(post, PostDTO.class);

        assertEquals(post.getId(), postDTO.getId());
        assertNull(postDTO.getTitle());
        assertNull(postDTO.getText());
        assertNull(postDTO.getTags());
    }
}