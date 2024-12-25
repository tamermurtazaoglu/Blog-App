package com.tamerm.blog_app.service;

import com.tamerm.blog_app.dto.PostDTO;
import com.tamerm.blog_app.dto.PostSummaryDTO;
import com.tamerm.blog_app.request.CreatePostRequest;

import java.util.List;

public interface PostService {
    PostDTO createPost(CreatePostRequest request);
    List<PostSummaryDTO> getAllPosts();
}
