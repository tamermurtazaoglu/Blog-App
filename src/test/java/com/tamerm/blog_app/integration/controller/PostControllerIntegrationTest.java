package com.tamerm.blog_app.integration.controller;

import com.tamerm.blog_app.BlogApplication;
import com.tamerm.blog_app.request.CreatePostRequest;
import com.tamerm.blog_app.request.UpdatePostRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the PostController.
 */
@SpringBootTest(classes = BlogApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PostControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test creating a new post and expect the created post to be returned.
     * @throws Exception if an error occurs during the request
     */
    @Test
    void createPost_ShouldReturnCreatedPost() throws Exception {
        CreatePostRequest request = new CreatePostRequest("Integration Test Title", "Integration Test Text", Collections.singletonList("TestTag"));

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Integration Test Title\",\"text\":\"Integration Test Text\",\"tags\":[\"TestTag\"]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Integration Test Title"));
    }

    /**
     * Test retrieving all post summaries and expect a list of posts.
     * @throws Exception if an error occurs during the request
     */
    @Test
    void getAllPostSummaries_ShouldReturnAllPosts() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").exists());
    }

    /**
     * Test retrieving a post by its ID and expect the post to be returned.
     * @throws Exception if an error occurs during the request
     */
    @Test
    void getPostById_ShouldReturnPost() throws Exception {
        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists());
    }

    /**
     * Test updating a post and expect the updated post to be returned.
     * @throws Exception if an error occurs during the request
     */
    @Test
    void updatePost_ShouldReturnUpdatedPost() throws Exception {
        UpdatePostRequest request = new UpdatePostRequest("Updated Integration Test Title", "Updated Integration Test Text", Collections.singletonList("UpdatedTestTag"));

        mockMvc.perform(put("/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Integration Test Title\",\"text\":\"Updated Integration Test Text\",\"tags\":[\"UpdatedTestTag\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Integration Test Title"));
    }

    /**
     * Test deleting a post and expect no content to be returned.
     * @throws Exception if an error occurs during the request
     */
    @Test
    void deletePost_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/posts/1"))
                .andExpect(status().isNoContent());
    }
}