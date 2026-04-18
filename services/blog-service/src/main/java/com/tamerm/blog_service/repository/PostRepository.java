package com.tamerm.blog_service.repository;

import com.tamerm.blog_service.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByTags_Name(String tagName, Pageable pageable);
    void deleteAllByUserId(Long userId);
}
