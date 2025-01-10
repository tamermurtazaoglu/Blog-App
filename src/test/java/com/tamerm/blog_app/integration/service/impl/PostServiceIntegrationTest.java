package com.tamerm.blog_app.integration.service.impl;

import com.tamerm.blog_app.BlogApplication;
import com.tamerm.blog_app.dto.PostDTO;
import com.tamerm.blog_app.exception.ResourceNotFoundException;
import com.tamerm.blog_app.request.CreatePostRequest;
import com.tamerm.blog_app.request.UpdatePostRequest;
import com.tamerm.blog_app.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the PostService.
 */
@SpringBootTest(classes = BlogApplication.class)
@Transactional
public class PostServiceIntegrationTest {

    @Autowired
    private PostService postService;

    /**
     * Test creating a new post and expect the created post to be returned.
     */
    @Test
    void createPost_ShouldReturnCreatedPost() {
        CreatePostRequest request = new CreatePostRequest("Integration Test Title", "Integration Test Text", Collections.singletonList("TestTag"));
        PostDTO createdPost = postService.createPost(request);

        assertNotNull(createdPost);
        assertEquals("Integration Test Title", createdPost.getTitle());
    }

    /**
     * Test retrieving a post by its ID and expect the post to be returned.
     */
    @Test
    void getPostById_ShouldReturnPost() {
        CreatePostRequest request = new CreatePostRequest("Integration Test Title", "Integration Test Text", Collections.singletonList("TestTag"));
        PostDTO createdPost = postService.createPost(request);

        PostDTO retrievedPost = postService.getPostById(createdPost.getId());

        assertNotNull(retrievedPost);
        assertEquals("Integration Test Title", retrievedPost.getTitle());
    }

    /**
     * Test updating a post and expect the updated post to be returned.
     */
    @Test
    void updatePost_ShouldReturnUpdatedPost() {
        CreatePostRequest createRequest = new CreatePostRequest("Integration Test Title", "Integration Test Text", Collections.singletonList("TestTag"));
        PostDTO createdPost = postService.createPost(createRequest);

        UpdatePostRequest updateRequest = new UpdatePostRequest("Updated Integration Test Title", "Updated Integration Test Text", Collections.singletonList("UpdatedTestTag"));
        PostDTO updatedPost = postService.updatePost(createdPost.getId(), updateRequest);

        assertNotNull(updatedPost);
        assertEquals("Updated Integration Test Title", updatedPost.getTitle());
    }

    /**
     * Test deleting a post and expect the post to be removed.
     */
    @Test
    void deletePost_ShouldRemovePost() {
        CreatePostRequest request = new CreatePostRequest("Integration Test Title", "Integration Test Text", Collections.singletonList("TestTag"));
        PostDTO createdPost = postService.createPost(request);

        postService.deletePost(createdPost.getId());

        assertThrows(ResourceNotFoundException.class, () -> postService.getPostById(createdPost.getId()));
    }
}