package com.tamerm.blog_service.service;

import com.tamerm.blog_service.dto.PostDTO;
import com.tamerm.blog_service.dto.PostSummaryDTO;
import com.tamerm.blog_service.request.CreatePostRequest;
import com.tamerm.blog_service.request.UpdatePostRequest;
import com.tamerm.blog_service.security.AuthenticatedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    PostDTO createPost(CreatePostRequest request, AuthenticatedUser principal);
    Page<PostSummaryDTO> getAllPosts(Pageable pageable);
    PostDTO getPostById(Long id);
    PostDTO updatePost(Long id, UpdatePostRequest request, AuthenticatedUser principal);
    void deletePost(Long postId, AuthenticatedUser principal);
    Page<PostSummaryDTO> getPostsByTagName(String tagName, Pageable pageable);
    Page<PostSummaryDTO> searchPosts(String query, Pageable pageable);
    void deleteAllByUserId(Long userId);
}
