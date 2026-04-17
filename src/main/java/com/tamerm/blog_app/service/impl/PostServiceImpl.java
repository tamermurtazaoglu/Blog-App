package com.tamerm.blog_app.service.impl;

import com.tamerm.blog_app.dto.PostDTO;
import com.tamerm.blog_app.dto.PostSummaryDTO;
import com.tamerm.blog_app.exception.BadRequestException;
import com.tamerm.blog_app.exception.ResourceNotFoundException;
import com.tamerm.blog_app.exception.UnauthorizedException;
import com.tamerm.blog_app.model.Post;
import com.tamerm.blog_app.model.Tag;
import com.tamerm.blog_app.model.User;
import com.tamerm.blog_app.repository.PostRepository;
import com.tamerm.blog_app.repository.UserRepository;
import com.tamerm.blog_app.request.CreatePostRequest;
import com.tamerm.blog_app.request.UpdatePostRequest;
import com.tamerm.blog_app.service.PostService;
import com.tamerm.blog_app.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service implementation for managing posts.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final TagService tagService;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    /**
     * Creates a new post.
     *
     * @param request the request object containing post details
     * @param userId the ID of the user creating the post
     * @return the created post
     * @throws BadRequestException if the post title is empty
     * @throws ResourceNotFoundException if the user is not found with the given ID
     */
    @Transactional
    @Override
    public PostDTO createPost(CreatePostRequest request, UserDetails userDetails) {
        User user = (User) userDetails;
        log.debug("Creating post for userId: {}", user.getId());
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            log.error("Post title cannot be empty");
            throw new BadRequestException("Post title cannot be empty");
        }

        Post post = modelMapper.map(request, Post.class);

        if (request.getTags() != null) {
            Set<Tag> tags = tagService.getOrCreateTags(request.getTags());
            post.setTags(tags);
        }

        post.setUser(user);

        Post savedPost = postRepository.save(post);
        log.info("Post created successfully with id: {}", savedPost.getId());
        return modelMapper.map(savedPost, PostDTO.class);
    }

    /**
     * Retrieves all post summaries.
     *
     * @return a list of post summaries
     */
    @Override
    public List<PostSummaryDTO> getAllPosts() {
        log.debug("Retrieving all posts");
        return postRepository.findAll().stream()
                .map(post -> new PostSummaryDTO(
                        post.getTitle(),
                        (post.getText() != null && post.getText().length() > 0)
                                ? post.getText().substring(0, Math.min(post.getText().length(), 50)) + "..."
                                : "No content available"
                ))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a post by its ID.
     *
     * @param id the ID of the post to retrieve
     * @return the retrieved post
     * @throws ResourceNotFoundException if the post is not found
     */
    @Override
    public PostDTO getPostById(Long id) {
        log.debug("Retrieving post with id: {}", id);
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + id));
        log.info("Post retrieved successfully with id: {}", id);
        return modelMapper.map(post, PostDTO.class);
    }

    /**
     * Updates an existing post.
     *
     * @param id      the ID of the post to update
     * @param request the request object containing updated post details
     * @return the updated post
     * @throws ResourceNotFoundException if the post is not found
     * @throws BadRequestException       if the post title is empty
     */
    @Transactional
    @Override
    public PostDTO updatePost(Long id, UpdatePostRequest request, UserDetails userDetails) {
        User user = (User) userDetails;
        log.debug("Updating post with id: {}", id);
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        if (!post.getUser().getId().equals(user.getId())) {
            log.error("User not authorized to update this post");
            throw new UnauthorizedException("User not authorized to update this post");
        }

        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            log.error("Post title cannot be empty");
            throw new BadRequestException("Post title cannot be empty");
        }

        modelMapper.typeMap(UpdatePostRequest.class, Post.class)
                .addMappings(mapper -> mapper.skip(Post::setTags));
        modelMapper.map(request, post);

        if (request.getTags() != null) {
            List<String> validTags = request.getTags().stream()
                    .filter(tagName -> tagName != null && !tagName.trim().isEmpty())
                    .collect(Collectors.toList());
            Set<Tag> tags = tagService.getOrCreateTags(validTags);
            post.setTags(tags);
        }

        if (post.getTags() != null) {
            Set<Tag> validTags = post.getTags().stream()
                    .filter(tag -> tag != null && tag.getName() != null && !tag.getName().trim().isEmpty())
                    .collect(Collectors.toSet());
            post.setTags(validTags);
        }

        Post updatedPost = postRepository.save(post);
        log.info("Post updated successfully with id: {}", updatedPost.getId());
        return modelMapper.map(updatedPost, PostDTO.class);
    }

    /**
     * Deletes a post by its ID and user ID.
     *
     * @param postId the ID of the post to delete
     * @param userId the ID of the user who owns the post
     * @param userDetails the details of the authenticated user
     * @throws ResourceNotFoundException if the post is not found with the given ID
     * @throws UnauthorizedException if the user is not authorized to delete the post
     */
    @Transactional
    @Override
    public void deletePost(Long postId, UserDetails userDetails) {
        User user = (User) userDetails;
        log.debug("Deleting post with id: {}", postId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + postId));

        if (!post.getUser().getId().equals(user.getId())) {
            log.error("User not authorized to delete this post");
            throw new UnauthorizedException("User not authorized to delete this post");
        }

        postRepository.delete(post);
        log.info("Post deleted successfully with id: {}", postId);
    }

    /**
     * Retrieves posts by tag name.
     *
     * @param tagName the name of the tag
     * @return a list of posts with the specified tag name
     */
    @Override
    public List<PostSummaryDTO> getPostsByTagName(String tagName) {
        log.debug("Retrieving posts with tag: {}", tagName);
        return postRepository.findAllByTags_Name(tagName).stream()
                .map(post -> new PostSummaryDTO(post.getTitle(), post.getText().substring(0, Math.min(post.getText().length(), 50)) + "..."))
                .collect(Collectors.toList());
    }
}