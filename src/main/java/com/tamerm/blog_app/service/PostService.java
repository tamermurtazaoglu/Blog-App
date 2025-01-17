package com.tamerm.blog_app.service;

import com.tamerm.blog_app.dto.PostDTO;
import com.tamerm.blog_app.dto.PostSummaryDTO;
import com.tamerm.blog_app.request.CreatePostRequest;
import com.tamerm.blog_app.request.UpdatePostRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface PostService {
    PostDTO createPost(CreatePostRequest request, Long userId, UserDetails userDetails);
    List<PostSummaryDTO> getAllPosts();
    PostDTO getPostById(Long id);
    PostDTO updatePost(Long id, UpdatePostRequest request);
    void deletePost(Long postId, Long userId, UserDetails userDetails);
    List<PostSummaryDTO> getPostsByTagName(String tagName);
}
