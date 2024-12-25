package com.tamerm.blog_app.repository;

import com.tamerm.blog_app.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing tags.
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * Finds a tag by its name.
     *
     * @param name the name of the tag
     * @return an optional containing the tag if found, or empty if not found
     */
    Optional<Tag> findByName(String name);
}