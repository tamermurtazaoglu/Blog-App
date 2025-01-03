package com.tamerm.blog_app.controller;

import com.tamerm.blog_app.dto.PostDTO;
import com.tamerm.blog_app.dto.PostSummaryDTO;
import com.tamerm.blog_app.exception.ResourceNotFoundException;
import com.tamerm.blog_app.request.CreatePostRequest;
import com.tamerm.blog_app.request.UpdatePostRequest;
import com.tamerm.blog_app.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    private PostDTO postDTO;
    private PostSummaryDTO postSummaryDTO;

    @BeforeEach
    void setUp() {
        postDTO = new PostDTO(1L, "Test Title", "Test Text", Collections.emptySet());
        postSummaryDTO = new PostSummaryDTO("Test Title", "Test Text...");
    }

    /**
     * Tests that a post is created successfully.
     */
    @Test
    void createPost_ShouldReturnCreatedPost() throws Exception {
        CreatePostRequest request = new CreatePostRequest("Test Title", "Test Text", Collections.emptyList());
        Mockito.when(postService.createPost(any(CreatePostRequest.class))).thenReturn(postDTO);

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test Title\",\"text\":\"Test Text\",\"tags\":[]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    /**
     * Tests that all post summaries are retrieved successfully.
     */
    @Test
    void getAllPostSummaries_ShouldReturnAllPosts() throws Exception {
        Mockito.when(postService.getAllPosts()).thenReturn(List.of(postSummaryDTO));

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Title"));
    }

    /**
     * Tests that a post is retrieved by its ID successfully.
     */
    @Test
    void getPostById_ShouldReturnPost() throws Exception {
        Mockito.when(postService.getPostById(anyLong())).thenReturn(postDTO);

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    /**
     * Tests that a NotFound status is returned when the post is not found.
     */
    @Test
    void getPostById_ShouldReturnNotFound_WhenPostDoesNotExist() throws Exception {
        Mockito.when(postService.getPostById(anyLong())).thenThrow(new ResourceNotFoundException("Post not found"));

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests that a post is updated successfully.
     */
    @Test
    void updatePost_ShouldReturnUpdatedPost() throws Exception {
        UpdatePostRequest request = new UpdatePostRequest("Updated Title", "Updated Text", Collections.emptyList());
        Mockito.when(postService.updatePost(anyLong(), any(UpdatePostRequest.class))).thenReturn(postDTO);

        mockMvc.perform(put("/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Title\",\"text\":\"Updated Text\",\"tags\":[]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    /**
     * Tests that a post is deleted successfully.
     */
    @Test
    void deletePost_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/posts/1"))
                .andExpect(status().isNoContent());
    }

    /**
     * Tests that posts are retrieved by tag name successfully.
     */
    @Test
    void getPostsByTag_ShouldReturnPosts() throws Exception {
        Mockito.when(postService.getPostsByTagName("tag")).thenReturn(List.of(postSummaryDTO));

        mockMvc.perform(get("/posts/byTag/tag"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Title"));
    }
}