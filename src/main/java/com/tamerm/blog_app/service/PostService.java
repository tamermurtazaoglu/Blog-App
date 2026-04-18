package com.tamerm.blog_app.service;

import com.tamerm.blog_app.dto.PostDTO;
import com.tamerm.blog_app.dto.PostSummaryDTO;
import com.tamerm.blog_app.request.CreatePostRequest;
import com.tamerm.blog_app.request.UpdatePostRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

public interface PostService {
    PostDTO createPost(CreatePostRequest request, UserDetails userDetails);
    Page<PostSummaryDTO> getAllPosts(Pageable pageable);
    PostDTO getPostById(Long id);
    PostDTO updatePost(Long id, UpdatePostRequest request, UserDetails userDetails);
    void deletePost(Long postId, UserDetails userDetails);
    Page<PostSummaryDTO> getPostsByTagName(String tagName, Pageable pageable);
}
