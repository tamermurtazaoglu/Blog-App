package com.tamerm.blog_app.service.impl;

import com.tamerm.blog_app.dto.PostDTO;
import com.tamerm.blog_app.dto.PostSummaryDTO;
import com.tamerm.blog_app.exception.BadRequestException;
import com.tamerm.blog_app.model.Post;
import com.tamerm.blog_app.model.Tag;
import com.tamerm.blog_app.repository.PostRepository;
import com.tamerm.blog_app.request.CreatePostRequest;
import com.tamerm.blog_app.service.PostService;
import com.tamerm.blog_app.service.TagService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service implementation for managing posts.
 */
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final TagService tagService;
    private final ModelMapper modelMapper;

    /**
     * Creates a new post.
     *
     * @param request the request object containing post details
     * @return the created post
     * @throws BadRequestException if the post title is empty
     */
    @Override
    public PostDTO createPost(CreatePostRequest request) {
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new BadRequestException("Post title cannot be empty");
        }

        Post post = modelMapper.map(request, Post.class);

        if (request.getTags() != null) {
            Set<Tag> tags = tagService.getOrCreateTags(request.getTags());
            post.setTags(tags);
        }

        Post savedPost = postRepository.save(post);
        return modelMapper.map(savedPost, PostDTO.class);
    }

    /**
     * Retrieves all post summaries.
     *
     * @return a list of post summaries
     */
    @Override
    public List<PostSummaryDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(post -> new PostSummaryDTO(
                        post.getTitle(),
                        (post.getText() != null && post.getText().length() > 0)
                                ? post.getText().substring(0, Math.min(post.getText().length(), 50)) + "..."
                                : "No content available"
                ))
                .collect(Collectors.toList());
    }

}
