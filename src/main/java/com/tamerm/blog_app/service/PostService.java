package com.tamerm.blog_app.service;

import com.tamerm.blog_app.dto.PostDTO;
import com.tamerm.blog_app.dto.PostSummaryDTO;
import com.tamerm.blog_app.request.CreatePostRequest;
import com.tamerm.blog_app.request.UpdatePostRequest;

import java.util.List;

public interface PostService {
    PostDTO createPost(CreatePostRequest request);
    List<PostSummaryDTO> getAllPosts();
    PostDTO getPostById(Long id);
    PostDTO updatePost(Long id, UpdatePostRequest request);
    void deletePost(Long id);
    List<PostSummaryDTO> getPostsByTagName(String tagName);
}
