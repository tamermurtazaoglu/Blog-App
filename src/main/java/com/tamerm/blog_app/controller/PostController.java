package com.tamerm.blog_app.controller;

import com.tamerm.blog_app.dto.PostDTO;
import com.tamerm.blog_app.dto.PostSummaryDTO;
import com.tamerm.blog_app.request.CreatePostRequest;
import com.tamerm.blog_app.request.UpdatePostRequest;
import com.tamerm.blog_app.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing posts.
 */
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * Create a new post.
     *
     * @param request the request object containing post details
     * @return the created post
     */
    @PostMapping
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody CreatePostRequest request) {
        PostDTO createdPost = postService.createPost(request);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    /**
     * Get all post summaries.
     *
     * @return a list of post summaries
     */
    @GetMapping
    public ResponseEntity<List<PostSummaryDTO>> getAllPostSummaries() {
        List<PostSummaryDTO> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    /**
     * Update an existing post.
     *
     * @param id the ID of the post to update
     * @param request the request object containing updated post details
     * @return the updated post
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable Long id, @Valid @RequestBody UpdatePostRequest request) {
        PostDTO postDTO = postService.updatePost(id, request);
        return ResponseEntity.ok(postDTO);
    }

    /**
     * Get posts by tag name.
     *
     * @param tagName the name of the tag
     * @return a list of posts with the specified tag name
     */
    @GetMapping("/byTag/{tagName}")
    public ResponseEntity<List<PostSummaryDTO>> getPostsByTag(@PathVariable String tagName) {
        List<PostSummaryDTO> posts = postService.getPostsByTagName(tagName);
        return ResponseEntity.ok(posts);
    }
}