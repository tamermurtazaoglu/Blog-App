package com.tamerm.blog_service.controller;

import com.tamerm.blog_service.dto.PostDTO;
import com.tamerm.blog_service.dto.PostSummaryDTO;
import com.tamerm.blog_service.request.CreatePostRequest;
import com.tamerm.blog_service.request.UpdatePostRequest;
import com.tamerm.blog_service.security.AuthenticatedUser;
import com.tamerm.blog_service.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDTO> createPost(
            @Valid @RequestBody CreatePostRequest request,
            @AuthenticationPrincipal AuthenticatedUser principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(request, principal));
    }

    @GetMapping
    public ResponseEntity<Page<PostSummaryDTO>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.getAllPosts(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePostRequest request,
            @AuthenticationPrincipal AuthenticatedUser principal) {
        return ResponseEntity.ok(postService.updatePost(id, request, principal));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal AuthenticatedUser principal) {
        postService.deletePost(postId, principal);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/byTag/{tagName}")
    public ResponseEntity<Page<PostSummaryDTO>> getPostsByTag(
            @PathVariable String tagName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.getPostsByTagName(tagName, PageRequest.of(page, size)));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PostSummaryDTO>> searchPosts(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.searchPosts(q, PageRequest.of(page, size)));
    }
}
