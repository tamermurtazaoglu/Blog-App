package com.tamerm.blog_app.repository;

import com.tamerm.blog_app.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing posts.
 */
public interface PostRepository extends JpaRepository<Post, Long> {
}
