package com.tamerm.blog_app.unit.repository;

import com.tamerm.blog_app.model.Post;
import com.tamerm.blog_app.model.Tag;
import com.tamerm.blog_app.model.User;
import com.tamerm.blog_app.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link PostRepository}.
 */
@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    private User user;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .displayName("Test user")
                .build();
    }

    /**
     * Tests that a post is persisted successfully.
     */
    @Test
    void save_ShouldPersistPost() {
        Post post = new Post();
        post.setTitle("Test Post");
        post.setText("Test Text");
        post.setUser(user);

        Post savedPost = postRepository.save(post);

        assertNotNull(savedPost.getId());
        assertEquals("Test Post", savedPost.getTitle());
    }

    /**
     * Tests that a post is returned when it exists in the database.
     */
    @Test
    void findById_ShouldReturnPost_WhenPostExists() {
        Post post = new Post();
        post.setTitle("Test Post");
        post.setText("Test Text");
        post.setUser(user);
        Post savedPost = postRepository.save(post);

        Optional<Post> foundPost = postRepository.findById(savedPost.getId());

        assertTrue(foundPost.isPresent());
        assertEquals("Test Post", foundPost.get().getTitle());
    }

    /**
     * Tests that an empty Optional is returned when the post does not exist.
     */
    @Test
    void findById_ShouldReturnEmpty_WhenPostDoesNotExist() {
        Optional<Post> foundPost = postRepository.findById(999L);

        assertFalse(foundPost.isPresent());
    }

    /**
     * Tests that all posts are returned.
     */
    @Test
    void findAll_ShouldReturnAllPosts() {
        Post post1 = new Post();
        post1.setTitle("Test Post 1");
        post1.setText("Test Text 1");
        post1.setUser(user);
        postRepository.save(post1);

        Post post2 = new Post();
        post2.setTitle("Test Post 2");
        post2.setText("Test Text 2");
        post2.setUser(user);
        postRepository.save(post2);

        List<Post> posts = postRepository.findAll();

        assertEquals(5, posts.size()); // because there are already 3 posts in the database
    }

    /**
     * Tests that posts with a specific tag are returned.
     */
    @Test
    void findAllByTags_Name_ShouldReturnPostsWithTag() {
        Tag tag = new Tag("tag1");
        Post post = new Post();
        post.setTitle("Test Post");
        post.setText("Test Text");
        post.setTags(Set.of(tag));
        post.setUser(user);
        postRepository.save(post);

        List<Post> posts = postRepository.findAllByTags_Name("tag1");

        assertNotNull(posts);
        assertFalse(posts.isEmpty());
        assertEquals("Test Post", posts.get(0).getTitle());
    }

    /**
     * Tests that an empty list is returned when no posts have the specified tag.
     */
    @Test
    void findAllByTags_Name_ShouldReturnEmptyList_WhenNoPostsWithTag() {
        List<Post> posts = postRepository.findAllByTags_Name("nonexistentTag");

        assertNotNull(posts);
        assertTrue(posts.isEmpty());
    }
}