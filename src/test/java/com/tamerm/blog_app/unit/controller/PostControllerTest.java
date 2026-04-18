package com.tamerm.blog_app.unit.controller;

import com.tamerm.blog_app.controller.PostController;
import com.tamerm.blog_app.dto.PostDTO;
import com.tamerm.blog_app.dto.PostSummaryDTO;
import com.tamerm.blog_app.exception.ResourceNotFoundException;
import com.tamerm.blog_app.request.CreatePostRequest;
import com.tamerm.blog_app.request.UpdatePostRequest;
import com.tamerm.blog_app.service.PostService;
import org.apache.coyote.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
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
    @Autowired
    private PostController postController;

    @BeforeEach
    void setUp() {
        postDTO = new PostDTO(1L, "Test Title", "Test Text", Collections.emptySet());
        postSummaryDTO = new PostSummaryDTO("Test Title", "Test Text...");
    }

    /**
     * Tests that a post is created successfully.
     */
    @Test
    void createPost_ShouldReturnCreatedPost() {
        CreatePostRequest request = new CreatePostRequest("Test Title", "Test Text", Collections.emptyList());
        Mockito.when(postService.createPost(any(CreatePostRequest.class), isNull())).thenReturn(postDTO);

        ResponseEntity<PostDTO> response = postController.createPost(request, null);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Test Title", response.getBody().getTitle());
    }

    /**
     * Tests that all post summaries are retrieved successfully.
     */
    @Test
    void getAllPostSummaries_ShouldReturnAllPosts() {
        Mockito.when(postService.getAllPosts(PageRequest.of(0, 10))).thenReturn(new PageImpl<>(List.of(postSummaryDTO)));

        ResponseEntity<Page<PostSummaryDTO>> response = postController.getAllPostSummaries(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Title", response.getBody().getContent().get(0).getTitle());
    }

    /**
     * Tests that a post is retrieved by its ID successfully.
     */
    @Test
    void getPostById_ShouldReturnPost() {
        Mockito.when(postService.getPostById(anyLong())).thenReturn(postDTO);

        ResponseEntity<PostDTO> response = postController.getPostById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Title", response.getBody().getTitle());
    }

    /**
     * Tests that a NotFound status is returned when the post is not found.
     */
    @Test
    void getPostById_ShouldReturnNotFound_WhenPostDoesNotExist() {
        Mockito.when(postService.getPostById(anyLong())).thenThrow(new ResourceNotFoundException("Post not found"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            postController.getPostById(1L);
        });

        assertEquals("Post not found", exception.getMessage());
    }

    /**
     * Tests that a post is updated successfully.
     */
    @Test
    void updatePost_ShouldReturnUpdatedPost() {
        UpdatePostRequest request = new UpdatePostRequest("Updated Title", "Updated Text", Collections.emptyList());
        Mockito.when(postService.updatePost(anyLong(), any(UpdatePostRequest.class), isNull())).thenReturn(postDTO);

        ResponseEntity<PostDTO> response = postController.updatePost(1L, request, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Title", response.getBody().getTitle());
    }

    /**
     * Tests that a post is deleted successfully.
     */
    @Test
    void deletePost_ShouldReturnNoContent() {
        ResponseEntity<Void> response = postController.deletePost(1L, null);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    /**
     * Tests that posts are retrieved by tag name successfully.
     */
    @Test
    void getPostsByTag_ShouldReturnPosts() {
        Mockito.when(postService.getPostsByTagName("tag", PageRequest.of(0, 10))).thenReturn(new PageImpl<>(List.of(postSummaryDTO)));

        ResponseEntity<Page<PostSummaryDTO>> response = postController.getPostsByTag("tag", 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Title", response.getBody().getContent().get(0).getTitle());
    }
}