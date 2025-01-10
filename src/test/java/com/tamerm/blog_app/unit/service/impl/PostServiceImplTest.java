package com.tamerm.blog_app.unit.service.impl;

import com.tamerm.blog_app.dto.PostDTO;
import com.tamerm.blog_app.dto.PostSummaryDTO;
import com.tamerm.blog_app.exception.BadRequestException;
import com.tamerm.blog_app.exception.ResourceNotFoundException;
import com.tamerm.blog_app.model.Post;
import com.tamerm.blog_app.model.Tag;
import com.tamerm.blog_app.repository.PostRepository;
import com.tamerm.blog_app.request.CreatePostRequest;
import com.tamerm.blog_app.request.UpdatePostRequest;
import com.tamerm.blog_app.service.TagService;
import com.tamerm.blog_app.service.impl.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PostServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private TagService tagService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private TypeMap<UpdatePostRequest, Post> typeMap;

    @InjectMocks
    private PostServiceImpl postService;

    private Post post;
    private PostDTO postDTO;
    private CreatePostRequest createPostRequest;
    private UpdatePostRequest updatePostRequest;

    @BeforeEach
    void setUp() {
        post = new Post(1L, "Test Title", "Test Text", Collections.emptySet());
        postDTO = new PostDTO(1L, "Test Title", "Test Text", Collections.emptySet());
        createPostRequest = new CreatePostRequest("Test Title", "Test Text", Collections.emptyList());
        updatePostRequest = new UpdatePostRequest("Updated Title", "Updated Text", Collections.emptyList());
    }

    /**
     * Tests that a post is created successfully.
     */
    @Test
    void createPost_ShouldReturnCreatedPost() {
        when(modelMapper.map(any(CreatePostRequest.class), eq(Post.class))).thenReturn(post);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(modelMapper.map(any(Post.class), eq(PostDTO.class))).thenReturn(postDTO);

        PostDTO result = postService.createPost(createPostRequest);

        assertNotNull(result);
        assertEquals(postDTO.getTitle(), result.getTitle());
        verify(postRepository, times(1)).save(post);
    }

    /**
     * Tests that a BadRequestException is thrown when the post title is empty.
     */
    @Test
    void createPost_ShouldThrowBadRequestException_WhenTitleIsEmpty() {
        createPostRequest.setTitle("");

        assertThrows(BadRequestException.class, () -> postService.createPost(createPostRequest));
    }

    /**
     * Tests that a BadRequestException is thrown when the post title is null.
     */
    @Test
    void createPost_ShouldThrowBadRequestException_WhenTitleIsNull() {
        createPostRequest.setTitle(null);

        assertThrows(BadRequestException.class, () -> postService.createPost(createPostRequest));
    }

    /**
     * Tests that tags are handled properly when creating a post.
     */
    @Test
    void createPost_ShouldHandleTagsProperly() {
        createPostRequest.setTags(List.of("tag1", "tag2"));
        Set<Tag> tags = Set.of(new Tag("tag1"), new Tag("tag2"));
        when(tagService.getOrCreateTags(anyList())).thenReturn(tags);
        when(modelMapper.map(any(CreatePostRequest.class), eq(Post.class))).thenReturn(post);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(modelMapper.map(any(Post.class), eq(PostDTO.class))).thenReturn(postDTO);

        PostDTO result = postService.createPost(createPostRequest);

        assertNotNull(result);
        assertEquals(postDTO.getTitle(), result.getTitle());
        verify(tagService, times(1)).getOrCreateTags(anyList());
        verify(postRepository, times(1)).save(post);
    }

    /**
     * Tests that all posts are retrieved successfully.
     */
    @Test
    void getAllPosts_ShouldReturnAllPosts() {
        when(postRepository.findAll()).thenReturn(List.of(post));

        List<PostSummaryDTO> result = postService.getAllPosts();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("Test Title", result.get(0).getTitle());
        verify(postRepository, times(1)).findAll();
    }

    /**
     * Tests that a post is retrieved by its ID successfully.
     */
    @Test
    void getPostById_ShouldReturnPost_WhenPostExists() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(modelMapper.map(any(Post.class), eq(PostDTO.class))).thenReturn(postDTO);

        PostDTO result = postService.getPostById(1L);

        assertNotNull(result);
        assertEquals(postDTO.getTitle(), result.getTitle());
    }

    /**
     * Tests that a ResourceNotFoundException is thrown when the post does not exist.
     */
    @Test
    void getPostById_ShouldThrowResourceNotFoundException_WhenPostDoesNotExist() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> postService.getPostById(1L));
    }

    /**
     * Tests that a BadRequestException is thrown when the post title is null during update.
     */
    @Test
    void updatePost_ShouldThrowBadRequestException_WhenTitleIsNull() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        updatePostRequest.setTitle(null);

        assertThrows(BadRequestException.class, () -> postService.updatePost(1L, updatePostRequest));
    }

    /**
     * Tests that a ResourceNotFoundException is thrown when the post does not exist during update.
     */
    @Test
    void updatePost_ShouldThrowResourceNotFoundException_WhenPostDoesNotExist() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> postService.updatePost(1L, updatePostRequest));
    }

    /**
     * Tests that a post is deleted successfully.
     */
    @Test
    void deletePost_ShouldDeletePost_WhenPostExists() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        postService.deletePost(1L);

        verify(postRepository, times(1)).delete(post);
    }

    /**
     * Tests that a ResourceNotFoundException is thrown when the post does not exist during deletion.
     */
    @Test
    void deletePost_ShouldThrowResourceNotFoundException_WhenPostDoesNotExist() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> postService.deletePost(1L));
    }

    /**
     * Tests that posts are retrieved by tag name successfully.
     */
    @Test
    void getPostsByTagName_ShouldReturnPosts() {
        when(postRepository.findAllByTags_Name("tag")).thenReturn(List.of(post));

        List<PostSummaryDTO> result = postService.getPostsByTagName("tag");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("Test Title", result.get(0).getTitle());
        verify(postRepository, times(1)).findAllByTags_Name("tag");
    }

    /**
     * Tests that an empty list is returned when no posts with the specified tag name exist.
     */
    @Test
    void getPostsByTagName_ShouldReturnEmptyList_WhenNoPostsWithTag() {
        when(postRepository.findAllByTags_Name("tag")).thenReturn(Collections.emptyList());

        List<PostSummaryDTO> result = postService.getPostsByTagName("tag");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}