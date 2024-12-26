package com.tamerm.blog_app.repository;

import com.tamerm.blog_app.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for managing posts.
 */
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Find all posts by tag name.
     *
     * @param tagName the name of the tag
     * @return a list of posts with the specified tag name
     */
    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t.name = :tagName")
    List<Post> findAllByTags_Name(String tagName);
}
