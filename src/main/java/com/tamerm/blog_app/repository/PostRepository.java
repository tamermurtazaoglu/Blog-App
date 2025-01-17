package com.tamerm.blog_app.repository;

import com.tamerm.blog_app.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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

    /**
     * Find a post by its ID and user ID.
     *
     * @param postId the ID of the post
     * @param userId the ID of the user
     * @return an optional post
     */
    @Query("SELECT p FROM Post p WHERE p.id = :postId AND p.user.id = :userId")
    Optional<Post> findByIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);
}
