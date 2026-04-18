package com.tamerm.blog_service.service.impl;

import com.tamerm.blog_service.dto.PostDTO;
import com.tamerm.blog_service.dto.PostSummaryDTO;
import com.tamerm.blog_service.exception.BadRequestException;
import com.tamerm.blog_service.exception.ResourceNotFoundException;
import com.tamerm.blog_service.exception.UnauthorizedException;
import com.tamerm.blog_service.model.Post;
import com.tamerm.blog_service.model.Tag;
import com.tamerm.blog_service.repository.PostRepository;
import com.tamerm.blog_service.request.CreatePostRequest;
import com.tamerm.blog_service.request.UpdatePostRequest;
import com.tamerm.blog_service.security.AuthenticatedUser;
import com.tamerm.blog_service.service.PostService;
import com.tamerm.blog_service.service.TagService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.engine.search.query.SearchResult;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final TagService tagService;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;

    @Transactional
    @Override
    public PostDTO createPost(CreatePostRequest request, AuthenticatedUser principal) {
        log.debug("Creating post for userId={}", principal.getUserId());

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setText(request.getText());
        post.setUserId(principal.getUserId());

        if (request.getTags() != null) {
            Set<Tag> tags = tagService.getOrCreateTags(request.getTags());
            post.setTags(tags);
        }

        Post saved = postRepository.save(post);
        log.info("Post created with id={}", saved.getId());
        return modelMapper.map(saved, PostDTO.class);
    }

    @Override
    public Page<PostSummaryDTO> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(this::toSummary);
    }

    @Override
    public PostDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + id));
        return modelMapper.map(post, PostDTO.class);
    }

    @Transactional
    @Override
    public PostDTO updatePost(Long id, UpdatePostRequest request, AuthenticatedUser principal) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + id));

        if (!post.getUserId().equals(principal.getUserId())) {
            throw new UnauthorizedException("Not authorized to update this post");
        }

        post.setTitle(request.getTitle());
        post.setText(request.getText());

        if (request.getTags() != null) {
            List<String> validTagNames = request.getTags().stream()
                    .filter(n -> n != null && !n.trim().isEmpty())
                    .collect(Collectors.toList());
            post.setTags(tagService.getOrCreateTags(validTagNames));
        }

        Post updated = postRepository.save(post);
        log.info("Post updated with id={}", updated.getId());
        return modelMapper.map(updated, PostDTO.class);
    }

    @Transactional
    @Override
    public void deletePost(Long postId, AuthenticatedUser principal) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + postId));

        if (!post.getUserId().equals(principal.getUserId())) {
            throw new UnauthorizedException("Not authorized to delete this post");
        }

        postRepository.delete(post);
        log.info("Post deleted with id={}", postId);
    }

    @Override
    public Page<PostSummaryDTO> getPostsByTagName(String tagName, Pageable pageable) {
        return postRepository.findAllByTags_Name(tagName, pageable).map(this::toSummary);
    }

    @Override
    public Page<PostSummaryDTO> searchPosts(String query, Pageable pageable) {
        SearchResult<Post> result = Search.session(entityManager)
                .search(Post.class)
                .where(f -> f.simpleQueryString()
                        .fields("title", "text", "tags.name")
                        .matching(query))
                .fetch((int) pageable.getOffset(), pageable.getPageSize());

        List<PostSummaryDTO> content = result.hits().stream()
                .map(this::toSummary)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, result.total().hitCount());
    }

    @Transactional
    @Override
    public void deleteAllByUserId(Long userId) {
        postRepository.deleteAllByUserId(userId);
        log.info("Deleted all posts for userId={}", userId);
    }

    private PostSummaryDTO toSummary(Post post) {
        String preview = (post.getText() != null && !post.getText().isEmpty())
                ? post.getText().substring(0, Math.min(post.getText().length(), 50)) + "..."
                : "No content available";
        return new PostSummaryDTO(post.getTitle(), preview);
    }
}
