package com.tamerm.blog_app.integration.repository;

import com.tamerm.blog_app.BlogApplication;
import com.tamerm.blog_app.model.Post;
import com.tamerm.blog_app.model.Tag;
import com.tamerm.blog_app.repository.PostRepository;
import com.tamerm.blog_app.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the PostRepository.
 */
@SpringBootTest(classes = BlogApplication.class)
@Transactional
public class PostRepositoryIntegrationTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    /**
     * Test finding posts by tag name and expect posts to be returned.
     */
    @Test
    void findAllByTags_Name_ShouldReturnPosts() {
        Tag tag = new Tag("tag1");
        tagRepository.save(tag);

        Post post = new Post();
        post.setTitle("Test Post");
        post.setText("Test Text");
        post.setTags(Set.of(tag));
        postRepository.save(post);

        List<Post> posts = postRepository.findAllByTags_Name("tag1");

        assertNotNull(posts);
        assertFalse(posts.isEmpty());
        assertEquals("Test Post", posts.get(0).getTitle());
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