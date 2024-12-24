package com.tamerm.blog_app.repository;

import com.tamerm.blog_app.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing tags.
 */
public interface TagRepository extends JpaRepository<Tag, Long> {
}
