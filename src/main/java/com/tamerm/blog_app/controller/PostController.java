package com.tamerm.blog_app.controller;

import com.tamerm.blog_app.dto.PostDTO;
import com.tamerm.blog_app.dto.PostSummaryDTO;
import com.tamerm.blog_app.request.CreatePostRequest;
import com.tamerm.blog_app.request.UpdatePostRequest;
import com.tamerm.blog_app.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing posts.
 */
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Tag(name = "Post Controller", description = "APIs for managing posts")
@SecurityRequirement(name = "bearerAuth")
public class PostController {

    private final PostService postService;

    /**
     * Create a new post.
     *
     * @param request the request object containing post details
     * @param userId the ID of the user creating the post
     * @param userDetails the details of the authenticated user
     * @return the created post
     */
    @PostMapping
    @Operation(summary = "Create a new post", description = "Creates a new post for the authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Post created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    public ResponseEntity<PostDTO> createPost(
            @Valid @RequestBody CreatePostRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        PostDTO createdPost = postService.createPost(request, userDetails);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    /**
     * Get all post summaries.
     *
     * @return a list of post summaries
     */
    @GetMapping
    @Operation(summary = "Get all post summaries", description = "Retrieves a list of all post summaries")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of post summaries retrieved successfully")
    })
    public ResponseEntity<List<PostSummaryDTO>> getAllPostSummaries() {
        List<PostSummaryDTO> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    /**
     * Get a post by ID.
     *
     * @param id the ID of the post to retrieve
     * @return the retrieved post
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a post by ID", description = "Retrieves a post by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<PostDTO> getPostById(
            @PathVariable @Parameter(description = "ID of the post to retrieve") Long id) {
        PostDTO postDTO = postService.getPostById(id);
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    /**
     * Update an existing post.
     *
     * @param id the ID of the post to update
     * @param request the request object containing updated post details
     * @return the updated post
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing post", description = "Updates a post owned by the authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<PostDTO> updatePost(
            @PathVariable @Parameter(description = "ID of the post to update") Long id,
            @Valid @RequestBody UpdatePostRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        PostDTO postDTO = postService.updatePost(id, request, userDetails);
        return ResponseEntity.ok(postDTO);
    }

    /**
     * Delete a post by ID.
     *
     * @param postId the ID of the post to delete
     * @param userId the ID of the user who owns the post
     * @param userDetails the details of the authenticated user
     * @return a response entity with no content
     */
    @DeleteMapping("/{postId}")
    @Operation(summary = "Delete a post by ID", description = "Deletes a post owned by the authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Post deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<Void> deletePost(
            @PathVariable @Parameter(description = "ID of the post to delete") Long postId,
            @AuthenticationPrincipal UserDetails userDetails) {
        postService.deletePost(postId, userDetails);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get posts by tag name.
     *
     * @param tagName the name of the tag
     * @return a list of posts with the specified tag name
     */
    @GetMapping("/byTag/{tagName}")
    @Operation(summary = "Get posts by tag name", description = "Retrieves a list of posts with the specified tag name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of posts retrieved successfully")
    })
    public ResponseEntity<List<PostSummaryDTO>> getPostsByTag(
            @PathVariable @Parameter(description = "Name of the tag") String tagName) {
        List<PostSummaryDTO> posts = postService.getPostsByTagName(tagName);
        return ResponseEntity.ok(posts);
    }
}