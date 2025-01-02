package com.tamerm.blog_app.repository;

import com.tamerm.blog_app.model.Post;
import com.tamerm.blog_app.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    private Post post;

    @BeforeEach
    void setUp() {
        post = new Post();
        post.setTitle("Test Title");
        post.setText("Test Text");
        post.setTags(Set.of(new Tag("tag1"), new Tag("tag2")));
    }

    /**
     * Tests that a post is persisted successfully.
     */
    @Test
    void save_ShouldPersistPost() {
        Post savedPost = postRepository.save(post);
        assertNotNull(savedPost.getId());
        assertEquals(post.getTitle(), savedPost.getTitle());
    }

    /**
     * Tests that a post is retrieved by its ID successfully when it exists.
     */
    @Test
    void findById_ShouldReturnPost_WhenPostExists() {
        Post savedPost = postRepository.save(post);
        Optional<Post> foundPost = postRepository.findById(savedPost.getId());
        assertTrue(foundPost.isPresent());
        assertEquals(savedPost.getTitle(), foundPost.get().getTitle());
    }

    /**
     * Tests that an empty result is returned when the post does not exist.
     */
    @Test
    void findById_ShouldReturnEmpty_WhenPostDoesNotExist() {
        Optional<Post> foundPost = postRepository.findById(999L);
        assertFalse(foundPost.isPresent());
    }

    /**
     * Tests that all posts are retrieved successfully.
     */
    @Test
    void findAll_ShouldReturnAllPosts() {
        postRepository.save(post);
        List<Post> posts = postRepository.findAll();
        assertFalse(posts.isEmpty());
    }

    /**
     * Tests that posts are retrieved by tag name successfully.
     */
    @Test
    void findAllByTags_Name_ShouldReturnPostsWithTag() {
        postRepository.save(post);
        List<Post> posts = postRepository.findAllByTags_Name("tag1");
        assertFalse(posts.isEmpty());
        assertEquals(post.getTitle(), posts.get(0).getTitle());
    }

    /**
     * Tests that an empty list is returned when no posts with the specified tag name exist.
     */
    @Test
    void findAllByTags_Name_ShouldReturnEmptyList_WhenNoPostsWithTag() {
        List<Post> posts = postRepository.findAllByTags_Name("nonexistent");
        assertTrue(posts.isEmpty());
    }
}