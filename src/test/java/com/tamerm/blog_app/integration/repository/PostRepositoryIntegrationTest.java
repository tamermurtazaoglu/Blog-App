package com.tamerm.blog_app.integration.repository;

import com.tamerm.blog_app.BlogApplication;
import com.tamerm.blog_app.TestContainersConfig;
import com.tamerm.blog_app.model.Post;
import com.tamerm.blog_app.model.Tag;
import com.tamerm.blog_app.model.User;
import com.tamerm.blog_app.repository.PostRepository;
import com.tamerm.blog_app.repository.TagRepository;
import com.tamerm.blog_app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the PostRepository.
 */
@SpringBootTest(classes = BlogApplication.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = TestContainersConfig.class)
@Transactional
public class PostRepositoryIntegrationTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Test finding posts by tag name and expect posts to be returned.
     */
    @Test
    public void findAllByTags_Name_ShouldReturnPosts() {
        // Create and save a User entity
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setDisplayName("Test User");
        userRepository.save(user);

        // Create and save a Post entity
        Post post = new Post();
        post.setTitle("Test Post");
        post.setText("This is a test post.");
        post.setUser(user); // Set the user field
        postRepository.save(post);

        // Add a Tag to the Post
        Tag tag = new Tag();
        tag.setName("testtag");
        tagRepository.save(tag);
        post.getTags().add(tag);
        postRepository.save(post);

        // Fetch posts by tag name
        List<Post> posts = postRepository.findAllByTags_Name("testtag");

        // Assert that the posts list is not empty and contains the created post
        assertFalse(posts.isEmpty());
        assertTrue(posts.contains(post));
    }

    /**
     * Test finding posts by tag name and expect an empty list when no posts with the tag exist.
     */
    @Test
    void findAllByTags_Name_ShouldReturnEmptyList_WhenNoPostsWithTag() {
        List<Post> posts = postRepository.findAllByTags_Name("nonexistentTag");

        assertNotNull(posts);
        assertTrue(posts.isEmpty());
    }
}